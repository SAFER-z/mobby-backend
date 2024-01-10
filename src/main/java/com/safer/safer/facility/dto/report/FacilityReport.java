package com.safer.safer.facility.dto.report;

import java.util.LinkedHashMap;

public interface FacilityReport {

    String getTitle(String userName);

    String getMessageColor();

    LinkedHashMap<String,String> toMap();
}
