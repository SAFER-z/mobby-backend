package com.safer.safer.config.batch.tasklet;

import com.safer.safer.domain.util.CsvUtil;
import com.safer.safer.config.batch.dto.StationDto;
import com.safer.safer.domain.Station;
import com.safer.safer.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StationTasklet implements Tasklet {

    private final StationRepository stationRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/station.csv").getURI().getPath();
        List<StationDto> items = CsvUtil.readCsv(filePath, StationDto.class);

        List<Station> stations = items.stream()
                .map(StationDto::toEntity)
                .collect(Collectors.toList());
        stationRepository.saveAll(stations);

        return RepeatStatus.FINISHED;
    }
}
