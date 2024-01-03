package com.safer.safer.facility.dto;

import com.safer.safer.facility.domain.FacilityType;

public interface FacilityDistanceResponse {
    long getId();
    String getName();
    String getAddress();
    FacilityType getCategory();
    double getDistance();
}
