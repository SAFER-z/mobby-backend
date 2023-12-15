package com.safer.safer.batch.tasklet.stationFacility;

import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.dto.stationFacility.StationLiftDto;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.station.domain.Station;
import com.safer.safer.common.exception.NoSuchElementException;
import com.safer.safer.facility.domain.repository.FacilityRepository;
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

import static com.safer.safer.batch.util.BatchConstant.UTF_8;
import static com.safer.safer.common.exception.ExceptionCode.NO_SUCH_STATION;

@Component
@RequiredArgsConstructor
public class StationLiftTasklet implements Tasklet {

    private final StationRepository stationRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/new_station_lift.csv").getURI().getPath();
        List<StationLiftDto> items = CsvUtil.readCsv(filePath, UTF_8, StationLiftDto.class);
        List<Facility> lifts = new ArrayList<>();

        items.forEach(lift -> {
            String stationName = CsvUtil.parseStationName(lift.getStationName());
            String line = lift.getLine();
            String operator = lift.getOperatorType();
            StationKey stationKey = StationKey.of(stationName, line, operator);

            Station station = stationRepository.findByStationKey(stationKey)
                    .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION, stationName));

            lifts.add(lift.toEntity(station));
        });

        facilityRepository.saveAll(lifts);
        return RepeatStatus.FINISHED;
    }
}
