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
    private String weekdayOpenTime;
    @CsvBindByPosition(position = 7)
    private String weekdayCloseTime;
    @CsvBindByPosition(position = 8)
    private String weekendOpenTime;
    @CsvBindByPosition(position = 9)
    private String weekendCloseTime;
    @CsvBindByPosition(position = 10)
    private String operator;
    @CsvBindByPosition(position = 11)
    private String phoneNumber;

    public Facility toEntity() {
        return Facility.of(
                String.join(" ", name, FacilityType.WHEELCHAIR_CHARGER.getName()),
                FacilityType.WHEELCHAIR_CHARGER,
                GeometryUtil.getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude)),
                roadAddress.isBlank() ? streetAddress : roadAddress,
                detailLocation,
                String.join(";",
                        "operator="+operator,
                        "phoneNumber="+phoneNumber,
                        "weekdayOpeningHours="+String.join("~", weekdayOpenTime, weekdayCloseTime),
                        "weekendOpeningHours="+String.join("~", weekendOpenTime, weekendCloseTime)
                )
        );
    }
}
