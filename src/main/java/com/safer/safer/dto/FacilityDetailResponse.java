package com.safer.safer.dto;

import com.safer.safer.domain.Facility;

public record FacilityDetailResponse(
        String name,
        String address,
        String detailLocation,
        String additional,
        String imageUrl
) {
    public static FacilityDetailResponse from(Facility facility) {
        return new FacilityDetailResponse(
                facility.getName(),
                facility.getAddress(),
                facility.getDetailLocation(),
                facility.getAdditional(),
                facility.getImageUrl()
        );
    }
}
