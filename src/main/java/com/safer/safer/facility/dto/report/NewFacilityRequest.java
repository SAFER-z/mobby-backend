package com.safer.safer.facility.dto.report;

import java.util.Map;

public record NewFacilityRequest(
        String name,
        String address,
        String category,
        Map<String,String> detailInfo,
        double latitude,
        double longitude
) {
    public NewFacilityReport toDto(String imageUrl) {
        return new NewFacilityReport(
                name,
                address,
                category,
                detailInfo,
                imageUrl,
                latitude,
                longitude
        );
    }
}
