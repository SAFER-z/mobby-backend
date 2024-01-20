package com.safer.safer.facility.domain.repository;

import com.safer.safer.facility.dto.report.FacilityReport;

public interface FacilityReportRepository {

    void save(String key, FacilityReport report);

    FacilityReport find(String key);

}
