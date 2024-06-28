package com.safer.safer.facility.domain;

import com.safer.safer.common.exception.NoSuchElementException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.safer.safer.common.exception.ExceptionCode.NO_SUCH_FACILITY_TYPE;

@RequiredArgsConstructor
@Getter
public enum FacilityType {
    TOILET("화장실"),
    WHEELCHAIR_CHARGER("전동휠체어 급속충전기"),
    WHEELCHAIR_LIFT("휠체어리프트"),
    PARKING_LOT("주차구역"),
    ACCESSIBLE_RAMP("경사로"),
    ELEVATOR("엘리베이터"),
    WELFARE_FACILITY("복지시설"),
    ACCESSIBILITY("접근성 인증 가게");

    private final String name;

    public static FacilityType from(String type) {
        return Arrays.stream(values())
                .filter(facilityType -> facilityType.getName().equals(type))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_FACILITY_TYPE, type));
    }

    public static boolean isNotValidType(String type) {
        return Arrays.stream(values())
                .noneMatch(facilityType -> facilityType.name().equals(type));
    }
}
