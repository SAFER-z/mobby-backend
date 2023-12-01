package com.safer.safer.station.dto;

import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.station.domain.Station;

import java.util.Set;

public record StationResponse(
        Long id,
        String name,
        String line,
        String imageUrl,
        boolean hasAccessibleRamp,
        boolean hasLift,
        boolean hasAccessibleToilet,
        boolean hasRampToEnter,
        boolean hasElevator,
        boolean hasCharger,
        double latitude,
        double longitude
) {
    public static StationResponse from(Station station) {
        Set<Facility> facilities = station.getFacilities();

        return new StationResponse(
                station.getId(),
                station.getStationKey().getName(),
                station.getStationKey().getLine(),
                station.getImageUrl(),
                station.isRampAvailable(),
                facilities.stream().anyMatch(facility -> facility.getCategory().equals(FacilityType.WHEELCHAIR_LIFT)),
                facilities.stream().anyMatch(facility -> facility.getCategory().equals(FacilityType.ACCESSIBLE_TOILET)),
                facilities.stream().anyMatch(facility -> facility.getCategory().equals(FacilityType.ACCESSIBLE_RAMP)),
                facilities.stream().anyMatch(facility -> facility.getCategory().equals(FacilityType.ELEVATOR)),
                facilities.stream().anyMatch(facility -> facility.getCategory().equals(FacilityType.WHEELCHAIR_CHARGER)),
                station.getCoordinate().getX(),
                station.getCoordinate().getY()
        );
    }
}
