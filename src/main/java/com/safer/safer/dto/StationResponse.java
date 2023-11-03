package com.safer.safer.dto;

import com.safer.safer.domain.Station;

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
