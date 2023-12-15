package com.safer.safer.batch.tasklet.facility;

import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.dto.facility.ToiletDto;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.safer.safer.batch.util.BatchConstant.UTF_8;

@Component
@RequiredArgsConstructor
public class ToiletTasklet implements Tasklet {

    private final FacilityRepository facilityRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/new_toilet.csv").getURI().getPath();
        List<ToiletDto> items = CsvUtil.readCsv(filePath, UTF_8, ToiletDto.class);

        List<Facility> toilets = items.stream()
                .map(ToiletDto::toEntity)
                .toList();

        facilityRepository.saveAll(toilets);
        return RepeatStatus.FINISHED;
    }
}
