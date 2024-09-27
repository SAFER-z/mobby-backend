package com.safer.safer.batch.tasklet.facility;

import com.google.common.util.concurrent.RateLimiter;
import com.safer.safer.batch.dto.facility.ParkingLotDto;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.facility.domain.repository.CustomFacilityRepository;
import com.safer.safer.routing.infrastructure.tmap.TMapRequester;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static com.safer.safer.batch.util.BatchConstant.EUC_KR;

@Component
@RequiredArgsConstructor
public class ParkingLotTasklet implements Tasklet {

    private final CustomFacilityRepository facilityRepository;
    private final TMapRequester tMapRequester;
    private final RateLimiter rateLimiter = RateLimiter.create(1.5);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception{
        String filePath = new ClassPathResource("data/parking_lot.csv").getURI().getPath();
        List<ParkingLotDto> items = CsvUtil.readCsv(filePath, EUC_KR, ParkingLotDto.class);

        Flux<Facility> parkingLots = Flux.fromIterable(items)
                .flatMap(item -> {
                    rateLimiter.acquire();
                    return item.needsCoordinate()
                            ? tMapRequester.searchCoordinate(item.getAddress())
                            .map(item::toEntity)
                            .subscribeOn(Schedulers.boundedElastic())
                            : Mono.just(item.toEntity(null));
                });

        parkingLots.collectList()
                .flatMap(parkingLotList -> Mono.fromRunnable(() -> facilityRepository.saveAll(parkingLotList)))
                .subscribe();

        return RepeatStatus.FINISHED;
    }
}
