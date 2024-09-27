package com.safer.safer.batch.tasklet.stationFacility;

import com.google.common.util.concurrent.RateLimiter;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.List;

import static com.safer.safer.batch.util.BatchConstant.UTF_8;

@Component
@RequiredArgsConstructor
public class StationTasklet implements Tasklet {

    private final CustomStationRepository stationRepository;
    private final TMapRequester tMapRequester;
    private final RateLimiter rateLimiter = RateLimiter.create(2.5);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/new_station.csv").getURI().getPath();
        List<StationDto> items = CsvUtil.readCsv(filePath,UTF_8, StationDto.class);

        Flux<Station> stations = Flux.fromIterable(items)
                .flatMap(item -> {
                    rateLimiter.acquire();
                    OperatorType operatorType = OperatorType.from(item.getOperator());
                    String stationName = CsvUtil.parseStationName(item.getName());

                    return item.needsCoordinate()
                            ? tMapRequester.searchCoordinate(operatorType.getTMapKeyword(stationName, item.getLine()))
                            .map(item::toEntity)
                            .subscribeOn(Schedulers.boundedElastic())
                            : Mono.just(item.toEntity(null));
                });

        stations.collectList()
                .flatMap(stationsList -> Mono.fromRunnable(() -> stationRepository.saveAll(stationsList)))
                .subscribe();

        return RepeatStatus.FINISHED;
    }
}
