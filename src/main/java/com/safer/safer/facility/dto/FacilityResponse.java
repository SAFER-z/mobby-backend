package com.safer.safer.facility.dto;

import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;

public record FacilityResponse(
        Long id,
        String name,
        FacilityType category,
        double latitude,
        double longitude
) {
    public static FacilityResponse from(Facility facility) {
        return new FacilityResponse(
                facility.getId(),
                facility.getName(),
                facility.getCategory(),
                facility.getCoordinate().getX(),
                facility.getCoordinate().getY()
        );
    }
}
