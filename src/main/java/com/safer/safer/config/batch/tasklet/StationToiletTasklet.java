package com.safer.safer.config.batch.tasklet;

import com.safer.safer.domain.util.CsvUtil;
import com.safer.safer.config.batch.dto.StationToiletDto;
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

import static com.safer.safer.config.batch.tasklet.Constant.KORAIL;
import static com.safer.safer.config.batch.tasklet.Constant.REMOVAL_REGEX;
import static com.safer.safer.exception.ExceptionCode.NO_SUCH_STATION;

@Component
@RequiredArgsConstructor
public class StationToiletTasklet implements Tasklet {

    private final StationRepository stationRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/station_toilet.csv").getURI().getPath();
        String korailFilePath = new ClassPathResource("data/korail_toilet.csv").getURI().getPath();

        List<StationToiletDto> items = CsvUtil.readCsv(filePath, StationToiletDto.class);
        List<StationToiletDto> korailItems = CsvUtil.readCsv(korailFilePath, StationToiletDto.class);
        List<Facility> toilets = new ArrayList<>();

        items.forEach(toilet -> {
            String stationName = toilet.getStationName().replaceAll(REMOVAL_REGEX, "");
            Station station = stationRepository.findByNameAndLine(stationName, toilet.getLine())
                    .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION, stationName));

            toilets.add(toilet.toEntity(station));
        });
        korailItems.forEach(toilet -> {
            String stationName = toilet.getStationName().replaceAll(REMOVAL_REGEX, "");
            Station station = stationRepository.findByNameAndOperator(stationName, KORAIL)
                    .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION, stationName));
            toilets.add(toilet.toEntity(station));
        });

        facilityRepository.saveAll(toilets);

        return RepeatStatus.FINISHED;
    }
}
