package com.safer.safer.batch.tasklet.stationFacility;

import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.dto.stationFacility.StationToiletDto;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.repository.CustomFacilityRepository;
import com.safer.safer.station.domain.Station;
import com.safer.safer.common.exception.NoSuchElementException;
import com.safer.safer.station.domain.StationKey;
import com.safer.safer.station.domain.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.safer.safer.batch.util.BatchConstant.*;
import static com.safer.safer.common.exception.ExceptionCode.NO_SUCH_STATION;

@Component
@RequiredArgsConstructor
public class StationToiletTasklet implements Tasklet {

    private final StationRepository stationRepository;
    private final CustomFacilityRepository facilityRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/new_station_toilet.csv").getURI().getPath();

        List<StationToiletDto> items = CsvUtil.readCsv(filePath, UTF_8, StationToiletDto.class);
        List<Facility> toilets = new ArrayList<>();

        items.forEach(toilet -> {
            String stationName = CsvUtil.parseStationName(toilet.getStationName());
            String line = toilet.getLine();
            String operator = toilet.getOperatorType();
            StationKey stationKey = StationKey.of(stationName, line, operator);

            Station station = stationRepository.findByStationKey(stationKey)
                    .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION, stationName));

            toilets.add(toilet.toEntity(station));
        });

        facilityRepository.saveAll(toilets);
        return RepeatStatus.FINISHED;
    }
}
