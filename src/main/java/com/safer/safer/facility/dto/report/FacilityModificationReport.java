package com.safer.safer.facility.dto.report;

import com.safer.safer.facility.dto.FacilityDetailResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class FacilityModificationReport implements FacilityReportConvertible {
    private final String currentName;
    private final String currentAddress;
    private final String currentCategory;
    private final Map<String,String> currentDetailInfo;
    private final String currentImageUrl;
    private final double currentLatitude;
    private final double currentLongitude;
    private final String name;
    private final String address;
    private final String category;
    private final Map<String,String> detailInfo;
    private final String imageUrl;
    private final double latitude;
    private final double longitude;

    private static final String TITLE = " 님으로부터 편의시설 수정 제보를 받았습니다: ";
    private static final String BLUE = "#008BEF";
    private static final String CURRENT = "기존 ";

    public static FacilityModificationReport from(
            FacilityDetailResponse facilityToUpdate,
            Point currentCoordinate,
            FacilityReport facilityReport
    ) {
        return new FacilityModificationReport(
                facilityToUpdate.name(),
                facilityToUpdate.address(),
                facilityToUpdate.category().name(),
                facilityToUpdate.detailInfo(),
                facilityToUpdate.imageUrl(),
                currentCoordinate.getX(),
                currentCoordinate.getY(),

                facilityReport.name(),
                facilityReport.address(),
                facilityReport.category(),
                facilityReport.detailInfo(),
                facilityReport.imageUrl(),
                facilityReport.latitude(),
                facilityReport.longitude()
        );
    }

    @Override
    public String getTitle(String userName, Long userId) {
        return String.format("%s(%d)%s", userName, userId, TITLE);
    }

    @Override
    public String getMessageColor() {
        return BLUE;
    }

    @Override
    public LinkedHashMap<String,String> toMap() {
        LinkedHashMap<String,String> resultMap = new LinkedHashMap<>();

        resultMap.put(CURRENT+"name", currentName);
        resultMap.put(CURRENT+"address", currentAddress);
        resultMap.put(CURRENT+"category", currentCategory);
        resultMap.put(CURRENT+"image", currentImageUrl);
        resultMap.put(CURRENT+"latitude", String.valueOf(currentLatitude));
        resultMap.put(CURRENT+"longitude", String.valueOf(currentLongitude));

        resultMap.putAll(currentDetailInfo.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> CURRENT.concat(entry.getKey()),
                        Map.Entry::getValue)
        ));

        resultMap.put("name", name);
        resultMap.put("address", address);
        resultMap.put("category", category);
        resultMap.put("image", imageUrl);
        resultMap.put("latitude", String.valueOf(latitude));
        resultMap.put("longitude", String.valueOf(longitude));
        resultMap.putAll(detailInfo);

        return resultMap;
    }
}
