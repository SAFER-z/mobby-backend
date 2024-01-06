package com.safer.safer.facility.dto;

import java.util.List;

public record FacilitiesDistanceResponse(List<FacilityDistanceResponse> facilities) {
    public static FacilitiesDistanceResponse of(final List<FacilityDistanceResponse> facilities) {
        return new FacilitiesDistanceResponse(facilities);
    }
}
