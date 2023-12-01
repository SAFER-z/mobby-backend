package com.safer.safer.batch.dto.facility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.batch.util.GeometryUtil;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;
import lombok.Getter;

@Getter
public class ChargerDto {
    @CsvBindByPosition(position = 0)
    private String name;
    @CsvBindByPosition(position = 1)
    private String address;
    @CsvBindByPosition(position = 2)
    private String latitude;
    @CsvBindByPosition(position = 3)
    private String longitude;
    @CsvBindByPosition(position = 4)
    private String detailLocation;
    @CsvBindByPosition(position = 5)
    private String weekdayOpenTime;
    @CsvBindByPosition(position = 6)
    private String weekdayCloseTime;
    @CsvBindByPosition(position = 7)
    private String weekendOpenTime;
    @CsvBindByPosition(position = 8)
    private String weekendCloseTime;
    @CsvBindByPosition(position = 9)
    private String phoneNumber;

    public Facility toEntity() {
        return Facility.of(
                String.join(" ", name, FacilityType.WHEELCHAIR_CHARGER.getName()),
                FacilityType.WHEELCHAIR_CHARGER,
                GeometryUtil.getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude)),
                address,
                String.join(";",
                        "detailLocation="+detailLocation,
                        "phoneNumber="+phoneNumber,
                        "weekdayOpeningHours="+String.join("~", weekdayOpenTime, weekdayCloseTime),
                        "weekendOpeningHours="+String.join("~", weekendOpenTime, weekendCloseTime)
                )
        );
    }
}
