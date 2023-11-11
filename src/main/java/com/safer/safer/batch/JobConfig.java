package com.safer.safer.batch;

import com.safer.safer.batch.tasklet.facility.*;
import com.safer.safer.batch.tasklet.stationFacility.*;
import com.safer.safer.facility.domain.repository.FacilityRepository;
import com.safer.safer.station.domain.repository.StationRepository;
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
                .next(DisabledEtcStep(jobRepository, transactionManager))
                .next(DisabledResidentialStep(jobRepository, transactionManager))
                .next(CommunityRehabStep(jobRepository, transactionManager))
                .next(MedicalRehabStep(jobRepository, transactionManager))
                .next(VocationalRehabStep(jobRepository, transactionManager))
                .next(ChargerStep(jobRepository, transactionManager))
                .next(ToiletStep(jobRepository, transactionManager))
                .next(ParkingLotStep(jobRepository, transactionManager))
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
    public Step ParkingLotStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("parkingLotStep", jobRepository)
                .tasklet(new ParkingLotTasklet(facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step DisabledEtcStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("disabledEtcStep", jobRepository)
                .tasklet(new DisabledEtcTasklet(facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step DisabledResidentialStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("disabledResidentialStep", jobRepository)
                .tasklet(new DisabledResidentialTasklet(facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step CommunityRehabStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("communityRehabStep", jobRepository)
                .tasklet(new CommunityRehabTasklet(facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step MedicalRehabStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("medicalRehabStep", jobRepository)
                .tasklet(new MedicalRehabTasklet(facilityRepository), transactionManager)
                .build();
    }

    @Bean
    public Step VocationalRehabStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("vocationalRehabStep", jobRepository)
                .tasklet(new VocationalRehabTasklet(facilityRepository), transactionManager)
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
