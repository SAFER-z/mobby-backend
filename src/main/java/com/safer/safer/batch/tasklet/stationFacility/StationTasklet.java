package com.safer.safer.batch.tasklet.stationFacility;

import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.dto.stationFacility.StationDto;
import com.safer.safer.station.domain.Station;
import com.safer.safer.station.domain.repository.CustomStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
;
import java.util.List;

import static com.safer.safer.batch.util.BatchConstant.UTF_8;

@Component
@RequiredArgsConstructor
public class StationTasklet implements Tasklet {

    private final CustomStationRepository stationRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/new_station.csv").getURI().getPath();
        List<StationDto> items = CsvUtil.readCsv(filePath,UTF_8, StationDto.class);

        List<Station> stations = items.stream()
                .map(StationDto::toEntity)
                .toList();

        stationRepository.saveAll(stations);
        return RepeatStatus.FINISHED;
    }
}
