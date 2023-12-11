package com.safer.safer.facility.dto;

import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public record FacilityDetailResponse(
        String name,
        FacilityType category,
        String address,
        Map<String, String> detailInfo,
        String imageUrl
) {
    public static FacilityDetailResponse from(Facility facility) {
        Map<String, String> detailInfo = null;

        if(StringUtils.hasText(facility.getDetailInfo())) {
            detailInfo = Arrays.stream(facility.getDetailInfo().split(";"))
                    .map(info -> info.split("="))
                    .collect(Collectors.toMap(
                            key -> key[0],
                            value -> value[1].trim()
                    ));
        }

        return new FacilityDetailResponse(
                facility.getName(),
                facility.getCategory(),
                facility.isInStation() ? facility.getStation().getAddress() : facility.getAddress(),
                detailInfo,
                facility.getImageUrl()
        );
    }
}
