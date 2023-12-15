package com.safer.safer.station.dto;

import com.safer.safer.facility.domain.Facility;
import com.safer.safer.station.domain.Station;

import java.util.Set;

import static com.safer.safer.facility.domain.FacilityType.*;

public record StationResponse(
        Long id,
        String name,
        String line,
        String address,
        String imageUrl,
        boolean hasAccessibleRamp,
        boolean hasLift,
        boolean hasToilet,
        boolean hasRamp,
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
                station.getAddress(),
                station.getImageUrl(),
                station.isRampAvailable(),
                facilities.stream().anyMatch(facility -> facility.getCategory().equals(WHEELCHAIR_LIFT)),
                facilities.stream().anyMatch(facility -> facility.getCategory().equals(TOILET)),
                facilities.stream().anyMatch(facility -> facility.getCategory().equals(ACCESSIBLE_RAMP)),
                facilities.stream().anyMatch(facility -> facility.getCategory().equals(ELEVATOR)),
                facilities.stream().anyMatch(facility -> facility.getCategory().equals(WHEELCHAIR_CHARGER)),
                station.getCoordinate().getX(),
                station.getCoordinate().getY()
        );
    }
}
