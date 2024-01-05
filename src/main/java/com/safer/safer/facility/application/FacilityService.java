package com.safer.safer.facility.application;

import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.facility.dto.*;
import com.safer.safer.common.exception.NoSuchElementException;
import com.safer.safer.facility.domain.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.safer.safer.common.exception.ExceptionCode.NO_SUCH_FACILITY;
import static com.safer.safer.common.exception.ExceptionCode.NO_SUCH_FACILITY_TYPE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacilityService {
    private final FacilityRepository facilityRepository;

    public FacilityDetailResponse findFacility(final Long facilityId) {
        return FacilityDetailResponse.from(facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_FACILITY)));
    }

    public FacilitiesResponse findFacilitiesWithin(CoordinateRequest coordinate, String category) {
        Point userCoordinate = coordinate.toPoint();

        if(StringUtils.hasText(category))
            return findFacilitiesByCategoryWithin(userCoordinate, category);

        return FacilitiesResponse.of(facilityRepository.findFacilitiesWithin(userCoordinate).stream()
                .map(FacilityResponse::from)
                .toList());
    }

    public FacilitiesResponse findFacilitiesByCategoryWithin(Point userCoordinate, String category) {
        if(FacilityType.isNotValidType(category))
            throw new NoSuchElementException(NO_SUCH_FACILITY_TYPE, category);

        return FacilitiesResponse.of(facilityRepository.findFacilitiesByCategoryWithin(userCoordinate, category).stream()
                .map(FacilityResponse::from)
                .toList());
    }

    public FacilitiesDistanceResponse findFacilitiesWithDistance(CoordinateRequest coordinate, String category) {
        Point userCoordinate = coordinate.toPoint();

        return FacilitiesDistanceResponse.of(
                facilityRepository.findFacilitiesWithDistance(userCoordinate, category)
        );
    }

    public List<FacilityDistanceResponse> searchFacilities(String query, CoordinateRequest coordinate) {
        Point userCoordinate = coordinate.toPoint();

        return facilityRepository.findSearchResult(query, userCoordinate);
    }
}
