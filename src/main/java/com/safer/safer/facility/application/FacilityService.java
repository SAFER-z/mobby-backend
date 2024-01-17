package com.safer.safer.facility.application;

import com.safer.safer.batch.util.GeometryUtil;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.facility.dto.*;
import com.safer.safer.common.exception.NoSuchElementException;
import com.safer.safer.facility.domain.repository.FacilityRepository;
import com.safer.safer.facility.dto.report.FacilityReport;
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

    @Transactional
    public void saveFacility(FacilityReport creationReport) {
        Facility facility = Facility.of(
                creationReport.name(),
                FacilityType.from(creationReport.category()),
                GeometryUtil.getPoint(creationReport.latitude(), creationReport.longitude()),
                creationReport.address(),
                creationReport.detailInfo().toString());

        facilityRepository.save(facility);
    }

    @Transactional
    public void updateFacility(FacilityReport modificationReport) {
        Facility facility = facilityRepository.findById(modificationReport.facilityId())
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_FACILITY));

        double latitude = modificationReport.latitude();
        double longitude = modificationReport.longitude();

        if(StringUtils.hasText(modificationReport.name()))
            facility.updateName(modificationReport.name());

        if(StringUtils.hasText(modificationReport.address()))
            facility.updateAddress(modificationReport.address());

        if(StringUtils.hasText(modificationReport.category()))
            facility.updateCategory(FacilityType.from(modificationReport.category()));

        if(!modificationReport.detailInfo().isEmpty())
            facility.updateDetailInfo(modificationReport.detailInfoToString());

        if(StringUtils.hasText(modificationReport.imageUrl()))
            facility.updateImageUrl(modificationReport.imageUrl());

        if(GeometryUtil.isValidRange(latitude, longitude))
            facility.updateCoordinate(GeometryUtil.getPoint(latitude, longitude));
    }

    public List<FacilityDistanceResponse> searchFacilities(String query, CoordinateRequest coordinate) {
        Point userCoordinate = coordinate.toPoint();

        return facilityRepository.findSearchResult(query, userCoordinate);
    }
}
