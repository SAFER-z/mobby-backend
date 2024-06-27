package com.safer.safer.routing.dto;

import com.safer.safer.facility.dto.FacilityDistanceResponse;
import com.safer.safer.routing.dto.tmap.PlaceResponse;

import java.util.List;

public record SearchResponse(
        List<FacilityDistanceResponse> facilities,
        List<PlaceResponse> placesResponse
) {
    public static SearchResponse of(
            List<FacilityDistanceResponse> facilities,
            List<PlaceResponse> placesResponse
    ) {
        return new SearchResponse(facilities, placesResponse);
    }
}
