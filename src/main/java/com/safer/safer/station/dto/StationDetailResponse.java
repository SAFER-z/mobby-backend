package com.safer.safer.station.dto;

import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.dto.FacilityDetailResponse;
import com.safer.safer.station.domain.Station;

import java.util.List;
import java.util.Set;

public record StationDetailResponse(
        String name,
        String line,
        String address,
        String operator,
        String phoneNumber,
        String imageUrl,
        boolean hasAccessibleRamp,
        String accessibleArea,
        List<FacilityDetailResponse> facilities
) {
    public static StationDetailResponse from(Station station) {
        Set<Facility> facilities = station.getFacilities();

        return new StationDetailResponse(
                station.getStationKey().getName(),
                station.getStationKey().getLine(),
                station.getAddress(),
                station.getStationKey().getOperator(),
                station.getPhoneNumber(),
                station.getImageUrl(),
                station.isRampAvailable(),
                station.getAccessibleArea(),
                facilities.stream()
                        .map(FacilityDetailResponse::from)
                        .toList()
        );
    }
}
