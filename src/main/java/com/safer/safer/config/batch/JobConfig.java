package com.safer.safer.config.batch;

import com.safer.safer.config.batch.tasklet.*;
import com.safer.safer.repository.FacilityRepository;
import com.safer.safer.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final FacilityRepository facilityRepository;
    private final StationRepository stationRepository;

    @Bean
    public Job csvJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("csvJob", jobRepository)
                .start(stationStep(jobRepository, transactionManager))
                .next(stationChargerStep(jobRepository, transactionManager))
                .next(stationElevatorStep(jobRepository, transactionManager))
                .next(stationRampStep(jobRepository, transactionManager))
                .next(stationLiftStep(jobRepository, transactionManager))
                .next(stationToiletStep(jobRepository, transactionManager))
                .next(DisabledFacilityStep(jobRepository, transactionManager))
                .next(ChargerStep(jobRepository, transactionManager))
                .next(ToiletStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step stationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stationStep", jobRepository)
                .tasklet(new StationTasklet(stationRepository), transactionManager)
                .build();
    }

    @Bean
    public Step ToiletStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("toiletStep", jobRepository)
                .tasklet(new ToiletTasklet(facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step ChargerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("chargerStep", jobRepository)
                .tasklet(new ChargerTasklet(facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step DisabledFacilityStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("disabledFacilityStep", jobRepository)
                .tasklet(new DisabledFacilityTasklet(facilityRepository), transactionManager)
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
