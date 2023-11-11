package com.safer.safer.station.dto;

import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.facility.dto.FacilityDetailResponse;
import com.safer.safer.station.domain.Station;

import java.util.List;
import java.util.Set;

public record StationDetailResponse(
        String name,
        String address,
        String line,
        String operator,
        String imageUrl,
        boolean hasLift,
        boolean hasToilet,
        boolean hasRamp,
        boolean hasElevator,
        boolean hasCharger,
        List<FacilityDetailResponse> facilities
) {
    public static StationDetailResponse from(Station station) {
        Set<Facility> facilities = station.getFacilities();

        return new StationDetailResponse(
                station.getName(),
                station.getAddress(),
                station.getLine(),
                station.getOperator(),
                station.getImageUrl(),
                facilities.stream().anyMatch(facility -> facility.getType().equals(FacilityType.WHEELCHAIR_LIFT)),
                facilities.stream().anyMatch(facility -> facility.getType().equals(FacilityType.ACCESSIBLE_TOILET)),
                facilities.stream().anyMatch(facility -> facility.getType().equals(FacilityType.ACCESSIBLE_RAMP)),
                facilities.stream().anyMatch(facility -> facility.getType().equals(FacilityType.ELEVATOR)),
                facilities.stream().anyMatch(facility -> facility.getType().equals(FacilityType.WHEELCHAIR_CHARGER)),
                facilities.stream()
                        .map(FacilityDetailResponse::from)
                        .toList()
        );
    }
}
