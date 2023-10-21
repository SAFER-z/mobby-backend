package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.util.GeometryUtil;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;
import lombok.Getter;

@Getter
public class ChargerDto {
    @CsvBindByPosition(position = 0)
    private String name;
    @CsvBindByPosition(position = 1)
    private String roadAddress;
    @CsvBindByPosition(position = 2)
    private String streetAddress;
    @CsvBindByPosition(position = 3)
    private String latitude;
    @CsvBindByPosition(position = 4)
    private String longitude;
    @CsvBindByPosition(position = 5)
    private String detailLocation;
    @CsvBindByPosition(position = 6)
    private String weekdayStartTime;
    @CsvBindByPosition(position = 7)
    private String weekdayEndTime;
    @CsvBindByPosition(position = 8)
    private String weekendStartTime;
    @CsvBindByPosition(position = 9)
    private String weekendEndTime;
    @CsvBindByPosition(position = 10)
    private String operator;
    @CsvBindByPosition(position = 11)
    private String phoneNumber;

    public Facility toEntity() {
        return Facility.of(
                name,
                FacilityType.CHARGER,
                GeometryUtil.getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude)),
                roadAddress.isEmpty() ? streetAddress : roadAddress,
                detailLocation,
                String.join(";",
                        "시설운영주체:"+operator,
                        "전화번호:"+phoneNumber,
                        "평일 운영시간:"+String.join("~", weekdayStartTime, weekdayEndTime),
                        "주말 운영시간:"+String.join("~", weekendStartTime, weekendEndTime)
                )
        );
    }
}
