package com.safer.safer.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FacilityType {
    TOILET("화장실"),
    CHARGER("전동휠체어 급속충전기"),
    LIFT("휠체어 리프트"),
    PARKING_LOT("주차구역"),
    RAMP("휠체어 경사로"),
    ELEVATOR("엘리베이터"),
    RESIDENTIAL("장애인거주시설"),
    COMMUNITY_REHABILITATION("지역사회재활시설"),
    VOCATIONAL_REHABILITATION("장애인직업재활시설"),
    MEDICAL_REHABILITATION("장애인의료재활시설"),
    INDEPENDENT_LIVING("장애인자립생활센터");

    @Getter
    private final String name;
}
