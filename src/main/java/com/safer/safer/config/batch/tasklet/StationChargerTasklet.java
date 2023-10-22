package com.safer.safer.config.batch.tasklet;

import com.safer.safer.config.batch.dto.KorailChargerDto;
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

        List<StationChargerDto> items = CsvUtil.readCsv(filePath, EUC_KR, StationChargerDto.class);
        List<KorailChargerDto> korailItems = CsvUtil.readCsv(korailFilePath, EUC_KR, KorailChargerDto.class);
        List<Facility> chargers = new ArrayList<>();

        items.forEach(charger -> {
            String stationName = CsvUtil.parseStationName(charger.getStationName());
            String line = CsvUtil.parseLine(charger.getLine());

            Station station = stationRepository.findByNameAndLine(stationName, line)
                    .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION, stationName));

            chargers.add(charger.toEntity(station));
        });

        korailItems.forEach(charger -> {
            String stationName = CsvUtil.parseStationName(charger.getStationName());
            String line = CsvUtil.parseLine(charger.getLine());

            Station station = stationRepository.findByNameAndLine(stationName, line)
                    .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION, stationName));

            chargers.add(charger.toEntity(station));
        });

        facilityRepository.saveAll(chargers);
        return RepeatStatus.FINISHED;
    }
}
