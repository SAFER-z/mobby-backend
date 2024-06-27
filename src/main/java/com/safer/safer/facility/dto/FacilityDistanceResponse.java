package com.safer.safer.facility.dto;

import com.safer.safer.facility.domain.FacilityType;

public interface FacilityDistanceResponse {

    Long getId();
    String getName();
    String getAddress();
    FacilityType getCategory();
    double getLatitude();
    double getLongitude();
    double getDistance();
}
