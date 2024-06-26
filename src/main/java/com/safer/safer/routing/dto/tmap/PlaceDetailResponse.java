package com.safer.safer.routing.dto.tmap;

import org.springframework.util.StringUtils;

public record PlaceDetailResponse(
        String name,
        String category,
        String address,
        String phoneNumber,
        boolean parkable,
        String routeInfo,
        String description,
        String additionalInfo,
        String openingHours
) {
    public static PlaceDetailResponse from(SearchDetailResult searchDetailResult) {
        SearchDetailResult.PlaceDetail placeDetail = searchDetailResult.getPoiDetailInfo();

        return new PlaceDetailResponse(
                placeDetail.getName(),
                placeDetail.getBizCatName(),
                String.join(" ", placeDetail.getBldAddr(), placeDetail.getBldNo1()) +
                        (StringUtils.hasText(placeDetail.getBldNo2()) ? "-" + placeDetail.getBldNo2() : ""),
                placeDetail.getTel(),
                placeDetail.getParkFlag() == 1,
                placeDetail.getRouteInfo(),
                placeDetail.getDesc(),
                placeDetail.getAdditionalInfo(),
                placeDetail.getUseTime()
        );
    }
}
