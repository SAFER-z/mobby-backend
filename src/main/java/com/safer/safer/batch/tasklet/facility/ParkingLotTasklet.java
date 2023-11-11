package com.safer.safer.batch.tasklet.facility;

import com.safer.safer.batch.dto.facility.ParkingLotDto;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.facility.domain.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.safer.safer.batch.util.BatchConstant.EUC_KR;

@Component
@RequiredArgsConstructor
public class ParkingLotTasklet implements Tasklet {

    private final FacilityRepository facilityRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception{
        String filePath = new ClassPathResource("data/parking_lot.csv").getURI().getPath();
        List<ParkingLotDto> items = CsvUtil.readCsv(filePath, EUC_KR, ParkingLotDto.class);

        List<Facility> chargers = items.stream()
                .map(ParkingLotDto::toEntity)
                .toList();

        facilityRepository.saveAll(chargers);
        return RepeatStatus.FINISHED;
    }
}
