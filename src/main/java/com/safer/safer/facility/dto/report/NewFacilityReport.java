package com.safer.safer.facility.dto.report;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class NewFacilityReport implements FacilityReport {
    private final String name;
    private final String address;
    private final String category;
    private final Map<String,String> detailInfo;
    private final String imageUrl;
    private final double latitude;
    private final double longitude;

    private static final String TITLE = " 님으로부터 편의시설 생성 제보를 받았습니다: ";
    private static final String GREEN = "#07DB00";

    @Override
    public String getTitle(String userName) {
        return userName + TITLE;
    }

    @Override
    public String getMessageColor() {
        return GREEN;
    }

    @Override
    public LinkedHashMap<String,String> toMap() {
        LinkedHashMap<String,String> basicInfo = new LinkedHashMap<>();

        basicInfo.put("name", name);
        basicInfo.put("address", address);
        basicInfo.put("category", category);
        basicInfo.put("image", imageUrl);
        basicInfo.put("latitude", String.valueOf(latitude));
        basicInfo.put("longitude", String.valueOf(longitude));
        basicInfo.putAll(detailInfo);

        return basicInfo;
    }
}
