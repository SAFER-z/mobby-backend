package com.safer.safer.station.dto;

import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.dto.FacilityDetailResponse;
import com.safer.safer.station.domain.Station;

import java.util.List;
import java.util.Set;

public record StationDetailResponse(
        String name,
        String address,
        String line,
        String operator,
        String phoneNumber,
        String accessibleArea,
        List<FacilityDetailResponse> facilities
) {
    public static StationDetailResponse from(Station station) {
        Set<Facility> facilities = station.getFacilities();

        return new StationDetailResponse(
                station.getStationKey().getName(),
                station.getAddress(),
                station.getStationKey().getLine(),
                station.getStationKey().getOperator(),
                station.getPhoneNumber(),
                station.getImageUrl(),
                facilities.stream()
                        .map(FacilityDetailResponse::from)
                        .toList()
        );
    }
}
