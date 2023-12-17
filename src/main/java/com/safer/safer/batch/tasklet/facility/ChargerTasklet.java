package com.safer.safer.batch.tasklet.facility;

import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.dto.facility.ChargerDto;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.repository.CustomFacilityRepository;
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
public class ChargerTasklet implements Tasklet {

    private final CustomFacilityRepository facilityRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception{
        String filePath = new ClassPathResource("data/charger.csv").getURI().getPath();
        List<ChargerDto> items = CsvUtil.readCsv(filePath, EUC_KR, ChargerDto.class);

        List<Facility> chargers = items.stream()
                .map(ChargerDto::toEntity)
                .toList();

        facilityRepository.saveAll(chargers);
        return RepeatStatus.FINISHED;
    }
}
