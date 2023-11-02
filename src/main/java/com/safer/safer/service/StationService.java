package com.safer.safer.service;

import com.safer.safer.dto.*;
import com.safer.safer.exception.NoSuchElementException;
import com.safer.safer.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.safer.safer.exception.ExceptionCode.NO_SUCH_STATION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    public StationDetailResponse findStation(final Long stationId) {
        return StationDetailResponse.from(stationRepository.findById(stationId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION)));
    }

    public StationsResponse findStationsByDistance(CoordinateRequest coordinate) {
        Point userCoordinate = coordinate.toPoint();

        return StationsResponse.of(stationRepository.findAllByDistance(userCoordinate).stream()
                .map(StationResponse::from)
                .toList());
    }
}
