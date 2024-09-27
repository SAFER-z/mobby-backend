package com.safer.safer.batch;

import com.safer.safer.batch.tasklet.facility.*;
import com.safer.safer.batch.tasklet.stationFacility.*;
import com.safer.safer.facility.domain.repository.CustomFacilityRepository;
import com.safer.safer.routing.infrastructure.tmap.TMapRequester;
import com.safer.safer.station.domain.repository.CustomStationRepository;
import com.safer.safer.station.domain.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobConfig {

    private final CustomFacilityRepository facilityRepository;
    private final StationRepository stationRepository;
    private final CustomStationRepository customStationRepository;
    private final TMapRequester tMapRequester;
    private final TaskExecutor taskExecutor;

    @Bean
    public Job etlJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("데이터 저장 프로세스를 시작합니다.");
        return new JobBuilder("insertionJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitFlow(jobRepository, transactionManager))
                .build()
                .build();

    }

    @Bean
    public Flow splitFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor)
                .add(stationFlow(jobRepository, transactionManager), facilityFlow(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow facilityFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("facilityFlow")
                .split(taskExecutor)
                .add(
                        tolietFlow(jobRepository, transactionManager),
                        welfareFacilityFlow(jobRepository, transactionManager),
                        chargerFlow(jobRepository, transactionManager),
                        parkingLotFlow(jobRepository, transactionManager)
                )
                .build();
    }

    @Bean
    public Flow stationFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("stationFlow")
                .start(stationStep(jobRepository, transactionManager))
                .next(stationDependentFlow(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow stationDependentFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("stationDependentFlow")
                .split(taskExecutor)
                .add(stationElevatorFlow(jobRepository, transactionManager),
                        stationChargerFlow(jobRepository, transactionManager),
                        stationRampFlow(jobRepository, transactionManager),
                        stationLiftFlow(jobRepository, transactionManager),
                        stationToiletFlow(jobRepository, transactionManager)
                ).build();
    }

    @Bean
    public Flow stationElevatorFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("stationElevatorFlow")
                .start(stationElevatorStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow stationChargerFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("stationChargerFlow")
                .start(stationChargerStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow stationRampFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("stationRampFlow")
                .start(stationRampStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow stationLiftFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("stationLiftFlow")
                .start(stationLiftStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow stationToiletFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("stationToiletFlow")
                .start(stationToiletStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step stationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stationStep", jobRepository)
                .tasklet(new StationTasklet(customStationRepository, tMapRequester), transactionManager)
                .build();
    }

    @Bean
    public Flow tolietFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("toiletFlow")
                .start(toiletStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow chargerFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("chargerFlow")
                .start(chargerStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow parkingLotFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("parkingLotFlow")
                .start(parkingLotStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow welfareFacilityFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("welfareFacilityFlow")
                .start(welfareFacilityStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step toiletStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("toiletStep", jobRepository)
                .tasklet(new ToiletTasklet(facilityRepository, tMapRequester), transactionManager)
                .build();
    }

    @Bean
    public Step chargerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("chargerStep", jobRepository)
                .tasklet(new ChargerTasklet(facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step parkingLotStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("parkingLotStep", jobRepository)
                .tasklet(new ParkingLotTasklet(facilityRepository, tMapRequester), transactionManager)
                .build();
    }

    @Bean
    public Step welfareFacilityStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("welfareFacilityStep", jobRepository)
                .tasklet(new WelfareFacilityTasklet(facilityRepository, tMapRequester), transactionManager)
                .build();
    }

    @Bean
    public Step stationChargerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stationChargerStep", jobRepository)
                .tasklet(new StationChargerTasklet(stationRepository, facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step stationElevatorStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stationElevatorStep", jobRepository)
                .tasklet(new StationElevatorTasklet(stationRepository, facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step stationLiftStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stationLiftStep", jobRepository)
                .tasklet(new StationLiftTasklet(stationRepository, facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step stationRampStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stationRampStep", jobRepository)
                .tasklet(new StationRampTasklet(stationRepository, facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step stationToiletStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stationToiletStep", jobRepository)
                .tasklet(new StationToiletTasklet(stationRepository, facilityRepository), transactionManager)
                .build();
    }
}
