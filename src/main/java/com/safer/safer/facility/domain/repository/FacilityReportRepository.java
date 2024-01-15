package com.safer.safer.facility.domain.repository;

import com.safer.safer.facility.dto.report.FacilityReport;

public interface FacilityReportRepository {

    void save(Long userId, FacilityReport report);

    FacilityReport findReport(Long userId);

}
