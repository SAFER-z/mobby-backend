package com.safer.safer.routing.dto;

import com.safer.safer.facility.dto.FacilityDistanceResponse;
import com.safer.safer.routing.dto.tmap.TMapResponse;

import java.util.List;

public record SearchResponse(
        List<FacilityDistanceResponse> facilities,
        List<TMapResponse> tMapResponses
) {
    public static SearchResponse of(
            List<FacilityDistanceResponse> facilities,
            List<TMapResponse> tMapResponses
    ) {
        return new SearchResponse(facilities, tMapResponses);
    }
}
