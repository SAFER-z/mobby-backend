package com.safer.safer.batch.tasklet.stationFacility;

import com.safer.safer.batch.dto.stationFacility.KorailChargerDto;
import com.safer.safer.batch.util.BatchConstant;
import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.dto.stationFacility.StationChargerDto;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.station.domain.Station;
import com.safer.safer.common.exception.NoSuchElementException;
import com.safer.safer.facility.domain.repository.FacilityRepository;
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

import static com.safer.safer.common.exception.ExceptionCode.NO_SUCH_STATION;

@Component
@RequiredArgsConstructor
public class StationChargerTasklet implements Tasklet {

    private final StationRepository stationRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/station_charger.csv").getURI().getPath();
        String korailFilePath = new ClassPathResource("data/korail_charger.csv").getURI().getPath();

        List<StationChargerDto> items = CsvUtil.readCsv(filePath, BatchConstant.EUC_KR, StationChargerDto.class);
        List<KorailChargerDto> korailItems = CsvUtil.readCsv(korailFilePath, BatchConstant.EUC_KR, KorailChargerDto.class);
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
