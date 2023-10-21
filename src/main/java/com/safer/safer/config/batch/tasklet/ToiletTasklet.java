package com.safer.safer.config.batch.tasklet;

import com.safer.safer.domain.util.CsvUtil;
import com.safer.safer.config.batch.dto.ToiletDto;
import com.safer.safer.domain.Facility;
import com.safer.safer.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToiletTasklet implements Tasklet {

    private final FacilityRepository facilityRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/toilet.csv").getURI().getPath();
        List<ToiletDto> items = CsvUtil.readCsv(filePath, ToiletDto.class);

        List<Facility> toilets = items.stream()
                .map(ToiletDto::toEntity)
                .collect(Collectors.toList());
        facilityRepository.saveAll(toilets);

        return RepeatStatus.FINISHED;
    }
}
