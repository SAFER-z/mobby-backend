package com.safer.safer.service;

import com.safer.safer.dto.CoordinateRequest;
import com.safer.safer.dto.FacilitiesResponse;
import com.safer.safer.dto.FacilityDetailResponse;
import com.safer.safer.dto.FacilityResponse;
import com.safer.safer.exception.NoSuchElementException;
import com.safer.safer.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.safer.safer.exception.ExceptionCode.NO_SUCH_FACILITY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacilityService {
    private final FacilityRepository facilityRepository;

    public FacilityDetailResponse findFacility(final Long facilityId) {
        return FacilityDetailResponse.from(facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_FACILITY)));
    }

    public FacilitiesResponse findFacilitiesByDistance(CoordinateRequest coordinate, String category) {
        Point userCoordinate = coordinate.toPoint();

        if(StringUtils.hasText(category))
            return findFacilitiesByDistanceAndCategory(userCoordinate, category);

        return FacilitiesResponse.of(facilityRepository.findAllByDistance(userCoordinate).stream()
                .map(FacilityResponse::from)
                .toList());
    }

    public FacilitiesResponse findFacilitiesByDistanceAndCategory(Point userCoordinate, String category) {
        return FacilitiesResponse.of(facilityRepository.findAllByDistanceAndType(userCoordinate, category).stream()
                .map(FacilityResponse::from)
                .toList());
    }
}
