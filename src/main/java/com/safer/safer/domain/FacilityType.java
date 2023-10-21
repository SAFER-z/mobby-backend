package com.safer.safer.domain;

import com.safer.safer.exception.NoSuchElementException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.safer.safer.exception.ExceptionCode.NO_SUCH_FACILITY_TYPE;

@RequiredArgsConstructor
@Getter
public enum FacilityType {
    TOILET("화장실"),
    DISABLED_TOILET("장애인화장실"),
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

    private final String name;

    public static FacilityType from(String type) {
        return Arrays.stream(values())
                .filter(facilityType -> facilityType.name().equals(type))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_FACILITY_TYPE, type));
    }
}
