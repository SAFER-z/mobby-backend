package com.safer.safer.dto;

import com.safer.safer.domain.Facility;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public record FacilityDetailResponse(
        String name,
        String address,
        String detailLocation,
        Map<String, String> additional,
        String imageUrl
) {
    public static FacilityDetailResponse from(Facility facility) {
        Map<String, String> additional = null;

        if(StringUtils.hasText(facility.getAdditional())) {
            additional = Arrays.stream(facility.getAdditional().split(";"))
                    .map(info -> info.split("="))
                    .collect(Collectors.toMap(
                            key -> key[0],
                            value -> value[1].trim()
                    ));
        }

        return new FacilityDetailResponse(
                facility.getName(),
                facility.isInStation() ? facility.getStation().getAddress() : facility.getAddress(),
                facility.getDetailLocation(),
                additional,
                facility.getImageUrl()
        );
    }
}
