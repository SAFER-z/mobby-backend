package com.safer.safer.station.dto;

import com.safer.safer.station.domain.Station;

public record StationResponse(
        Long id,
        String name,
        String line,
        double latitude,
        double longitude
) {
    public static StationResponse from(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName(),
                station.getLine(),
                station.getCoordinate().getX(),
                station.getCoordinate().getY()
        );
    }
}
