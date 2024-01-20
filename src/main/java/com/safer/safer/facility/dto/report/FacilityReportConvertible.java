package com.safer.safer.facility.dto.report;

import java.util.LinkedHashMap;

public interface FacilityReportConvertible {

    String getTitle(String userName, Long userId);

    String getMessageColor();

    LinkedHashMap<String,String> toMap();
}
