package com.safer.safer.batch.tasklet.stationFacility;

import com.safer.safer.batch.util.BatchConstant;
import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.dto.stationFacility.StationDto;
import com.safer.safer.station.domain.Station;
import com.safer.safer.station.domain.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StationTasklet implements Tasklet {

    private final StationRepository stationRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/station.csv").getURI().getPath();
        List<StationDto> items = CsvUtil.readCsv(filePath, BatchConstant.UTF_8, StationDto.class);

        List<Station> stations = items.stream()
                .map(StationDto::toEntity)
                .toList();

        stationRepository.saveAll(stations);
        return RepeatStatus.FINISHED;
    }
}
