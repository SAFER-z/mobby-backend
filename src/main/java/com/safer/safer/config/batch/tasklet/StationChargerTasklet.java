package com.safer.safer.config.batch.tasklet;

import com.safer.safer.domain.util.CsvUtil;
import com.safer.safer.config.batch.dto.StationChargerDto;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.safer.safer.exception.ExceptionCode.NO_SUCH_STATION;
import static com.safer.safer.config.batch.tasklet.Constant.*;

@Component
@RequiredArgsConstructor
public class StationChargerTasklet implements Tasklet {

    private final StationRepository stationRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/station_charger.csv").getURI().getPath();
        String korailFilePath = new ClassPathResource("data/korail_charger.csv").getURI().getPath();

        List<StationChargerDto> items = CsvUtil.readCsv(filePath, StationChargerDto.class);
        List<StationChargerDto> korailItems = CsvUtil.readCsv(korailFilePath, StationChargerDto.class);
        List<Facility> chargers = new ArrayList<>();

        items.forEach(charger -> {
            String stationName = charger.getStationName().replaceAll(REMOVAL_REGEX, "");

            Station station = stationRepository.findByNameAndLine(stationName, charger.getLine())
                    .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION, stationName));
            chargers.add(charger.toEntity(station));
        });

        korailItems.forEach(charger -> {
            String stationName = charger.getStationName().replaceAll(REMOVAL_REGEX, "");

            Station station = stationRepository.findByNameAndOperator(stationName, KORAIL)
                    .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION, stationName));
            chargers.add(charger.toEntity(station));
        });

        facilityRepository.saveAll(chargers);
        return RepeatStatus.FINISHED;
    }
}
