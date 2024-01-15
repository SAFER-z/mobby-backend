package com.safer.safer.facility.dto.report;

import java.util.Map;

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
}
