package com.safer.safer.batch.tasklet.facility;

import com.google.common.util.concurrent.RateLimiter;
import com.safer.safer.batch.dto.facility.WelfareFacilityDto;
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
public class WelfareFacilityTasklet implements Tasklet {

    private final CustomFacilityRepository facilityRepository;
    private final TMapRequester tMapRequester;
    private final RateLimiter rateLimiter = RateLimiter.create(1.5);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String filePath = new ClassPathResource("data/welfare_facility.csv").getURI().getPath();
        List<WelfareFacilityDto> items = CsvUtil.readCsv(filePath, EUC_KR, WelfareFacilityDto.class);

        Flux<Facility> welfareFacilities = Flux.fromIterable(items)
                .flatMap(item -> {
                    rateLimiter.acquire();
                    return tMapRequester.searchCoordinate(item.getAddress())
                            .map(item::toEntity)
                            .subscribeOn(Schedulers.boundedElastic());
                });

        welfareFacilities.collectList()
                .flatMap(facilitiesList -> Mono.fromRunnable(() -> facilityRepository.saveAll(facilitiesList)))
                .subscribe();

        return RepeatStatus.FINISHED;
    }
}
