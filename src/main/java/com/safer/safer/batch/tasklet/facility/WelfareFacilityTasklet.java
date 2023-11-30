package com.safer.safer.batch.tasklet.facility;

import com.safer.safer.batch.dto.facility.WelfareFacilityDto;
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
public class WelfareFacilityTasklet implements Tasklet {

    private final FacilityRepository facilityRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/welfare_facility.csv").getURI().getPath();
        List<WelfareFacilityDto> items = CsvUtil.readCsv(filePath, EUC_KR, WelfareFacilityDto.class);

        List<Facility> welfareFacilities = items.stream()
                .map(WelfareFacilityDto::toEntity)
                .toList();

        facilityRepository.saveAll(welfareFacilities);
        return RepeatStatus.FINISHED;
    }
}
