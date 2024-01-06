package com.safer.safer.station.application;

import com.safer.safer.facility.dto.CoordinateRequest;
import com.safer.safer.common.exception.NoSuchElementException;
import com.safer.safer.station.domain.repository.StationRepository;
import com.safer.safer.station.dto.StationDetailResponse;
import com.safer.safer.station.dto.StationDistanceResponse;
import com.safer.safer.station.dto.StationResponse;
import com.safer.safer.station.dto.StationsResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.safer.safer.common.exception.ExceptionCode.NO_SUCH_STATION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    public StationDetailResponse findStation(final Long stationId) {
        return StationDetailResponse.from(stationRepository.findById(stationId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION)));
    }

    public StationsResponse findStationsWithin(CoordinateRequest coordinate) {
        Point userCoordinate = coordinate.toPoint();

        return StationsResponse.of(stationRepository.findStationsWithin(userCoordinate).stream()
                .map(StationResponse::from)
                .toList());
    }

    public List<StationDistanceResponse> searchStations(String query, CoordinateRequest coordinate) {
        Point userCoordinate = coordinate.toPoint();

        return stationRepository.findSearchResult(query, userCoordinate);
    }
}
