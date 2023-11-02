package com.safer.safer.dto;

import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;

public record FacilityResponse(
        Long id,
        String name,
        FacilityType type,
        double latitude,
        double longitude
) {
    public static FacilityResponse from(Facility facility) {
        return new FacilityResponse(
                facility.getId(),
                facility.getName(),
                facility.getType(),
                facility.getCoordinate().getX(),
                facility.getCoordinate().getY()
        );
    }
}
