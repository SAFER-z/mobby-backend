package com.safer.safer.facility.domain.repository;

import com.safer.safer.common.exception.NoSuchElementException;
import com.safer.safer.facility.dto.report.FacilityReport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

import static com.safer.safer.common.exception.ExceptionCode.NO_SUCH_FACILITY_REPORT;
import static java.lang.System.currentTimeMillis;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;

@Repository
@RequiredArgsConstructor
public class FacilityReportRepositoryImpl implements FacilityReportRepository {

    private final RedisTemplate<String,FacilityReport> redisTemplate;

    private final static int REPORT_EXPIRATION_DAYS = 7;

    @Override
    public void save(String key, FacilityReport report) {
        ZSetOperations<String, FacilityReport> zSetOps = redisTemplate.opsForZSet();
        Date expirationDate = Date.from(now().plus(REPORT_EXPIRATION_DAYS, DAYS));

        zSetOps.add(key, report, currentTimeMillis());
        redisTemplate.expireAt(key, expirationDate);
    }

    @Override
    public FacilityReport find(String key) {
        ZSetOperations<String, FacilityReport> zSetOps = redisTemplate.opsForZSet();

        return Optional.ofNullable(zSetOps.popMin(key))
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_FACILITY_REPORT))
                .getValue();
    }
}
