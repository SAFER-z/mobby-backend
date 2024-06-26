package com.safer.safer.routing.dto;

import com.safer.safer.facility.dto.FacilityDistanceResponse;
import com.safer.safer.routing.dto.tmap.PlaceResponse;

import java.util.List;

public record SearchResponse(
        List<FacilityDistanceResponse> facilities,
        List<PlaceResponse> placeResponse
) {
    public static SearchResponse of(
            List<FacilityDistanceResponse> facilities,
            List<PlaceResponse> placeResponse
    ) {
        return new SearchResponse(facilities, placeResponse);
    }
}
