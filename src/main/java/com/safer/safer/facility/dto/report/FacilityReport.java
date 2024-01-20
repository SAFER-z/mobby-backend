package com.safer.safer.facility.dto.report;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public record FacilityReport(
        Long facilityId,
        String name,
        String address,
        String category,
        Map<String,String> detailInfo,
        String imageUrl,
        double latitude,
        double longitude
) {
    public static FacilityReport from(FacilityReportRequest facilityReportRequest, String imageUrl) {
        return new FacilityReport(
                0L,
                facilityReportRequest.name(),
                facilityReportRequest.address(),
                facilityReportRequest.category(),
                facilityReportRequest.detailInfo(),
                imageUrl,
                facilityReportRequest.latitude(),
                facilityReportRequest.longitude()
        );
    }

    public static FacilityReport from(Long facilityId, FacilityReportRequest facilityReportRequest, String imageUrl) {
        return new FacilityReport(
                facilityId,
                facilityReportRequest.name(),
                facilityReportRequest.address(),
                facilityReportRequest.category(),
                facilityReportRequest.detailInfo(),
                imageUrl,
                facilityReportRequest.latitude(),
                facilityReportRequest.longitude()
        );
    }

    public String detailInfoToString() {
        return detailInfo.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(e -> String.join("=", e.getKey(), e.getValue()))
                .collect(Collectors.joining(";"));
    }
}
