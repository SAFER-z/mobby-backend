package com.safer.safer.batch;

import com.safer.safer.batch.tasklet.facility.*;
import com.safer.safer.batch.tasklet.stationFacility.*;
import com.safer.safer.facility.domain.repository.CustomFacilityRepository;
import com.safer.safer.routing.infrastructure.tmap.TMapRequester;
import com.safer.safer.station.domain.repository.CustomStationRepository;
import com.safer.safer.station.domain.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final CustomFacilityRepository facilityRepository;
    private final StationRepository stationRepository;
    private final CustomStationRepository customStationRepository;
    private final TMapRequester tMapRequester;

    @Bean
    public Job insertionJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("insertionJob", jobRepository)
                .start(splitFlow(jobRepository, transactionManager))
                .build()
                .build();
    }

    @Bean
    public Flow splitFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(stationFlow(jobRepository, transactionManager), facilityFlow(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow facilityFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("facilityFlow")
                .start(toiletStep(jobRepository, transactionManager))
                .next(welfareFacilityStep(jobRepository, transactionManager))
                .next(chargerStep(jobRepository, transactionManager))
                .next(parkingLotStep(jobRepository, transactionManager))
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
                .split(taskExecutor())
                .add(stationElevatorFlow(jobRepository, transactionManager), stationFacilityFlow(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow stationElevatorFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("stationElevatorFlow")
                .start(stationElevatorStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow stationFacilityFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new FlowBuilder<SimpleFlow>("stationFacilityFlow")
                .start(stationChargerStep(jobRepository, transactionManager))
                .next(stationRampStep(jobRepository, transactionManager))
                .next(stationLiftStep(jobRepository, transactionManager))
                .next(stationToiletStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step stationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stationStep", jobRepository)
                .tasklet(new StationTasklet(customStationRepository, tMapRequester), transactionManager)
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

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("batch_thread");
    }
}
