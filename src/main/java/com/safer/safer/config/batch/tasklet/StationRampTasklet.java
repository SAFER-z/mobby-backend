package com.safer.safer.config.batch.tasklet;

import com.safer.safer.domain.util.CsvUtil;
import com.safer.safer.config.batch.dto.StationRampDto;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.Station;
import com.safer.safer.exception.NoSuchElementException;
import com.safer.safer.repository.FacilityRepository;
import com.safer.safer.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.safer.safer.config.batch.tasklet.Constant.*;
import static com.safer.safer.exception.ExceptionCode.NO_SUCH_STATION;

@Component
@RequiredArgsConstructor
public class StationRampTasklet implements Tasklet {

    private final StationRepository stationRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/station_ramp.csv").getURI().getPath();
        List<StationRampDto> items = CsvUtil.readCsv(filePath, EUC_KR, StationRampDto.class);
        List<Facility> ramps = new ArrayList<>();

        items.forEach(ramp -> {
            String stationName = CsvUtil.parseStationName(ramp.getStationName());
            String line = CsvUtil.parseLine(ramp.getLine());

            Station station = stationRepository.findByNameAndLine(stationName, line)
                        .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION, stationName+line));

            ramps.add(ramp.toEntity(station));
        });

        facilityRepository.saveAll(ramps);
        return RepeatStatus.FINISHED;
    }
}
