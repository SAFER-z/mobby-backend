package com.safer.safer.facility.dto.report;

import java.util.Map;

public record FacilityRequest(
        String name,
        String address,
        String category,
        Map<String,String> detailInfo,
        double latitude,
        double longitude
) {
}
