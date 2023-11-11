package com.safer.safer.facility.dto;

import java.util.List;

public record FacilitiesResponse(
    List<FacilityResponse> facilities
) {
    public static FacilitiesResponse of(final List<FacilityResponse> facilities) {
        return new FacilitiesResponse(facilities);
    }
}
