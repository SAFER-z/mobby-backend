package com.safer.safer.dto;

import java.util.List;

public record StationsResponse(
        List<StationResponse> stations
) {
    public static StationsResponse of(final List<StationResponse> stations) {
        return new StationsResponse(stations);
    }
}
