package com.safer.safer.batch.tasklet.stationFacility;

import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.dto.stationFacility.StationDto;
import com.safer.safer.routing.infrastructure.tmap.TMapRequester;
import com.safer.safer.station.domain.OperatorType;
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
    private final TMapRequester tMapRequester;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/new_station.csv").getURI().getPath();
        List<StationDto> items = CsvUtil.readCsv(filePath,UTF_8, StationDto.class);

        List<Station> stations = items.stream()
                .map(item -> {
                    OperatorType operatorType = OperatorType.from(item.getOperator());
                    String stationName = CsvUtil.parseStationName(item.getName());

                    return item.toEntity(item.needsCoordinate() ? tMapRequester.searchCoordinate(
                            operatorType.getTMapKeyword(stationName, item.getLine())) : null
                    );
                })
                .toList();

        stationRepository.saveAll(stations);
        return RepeatStatus.FINISHED;
    }
}
