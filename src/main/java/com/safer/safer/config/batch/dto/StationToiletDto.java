package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;
import com.safer.safer.domain.Station;
import com.safer.safer.domain.util.GeometryUtil;
import lombok.Getter;

@Getter
public class StationToiletDto {
    @CsvBindByPosition(position = 0)
    private String line;
    @CsvBindByPosition(position = 1)
    private String stationName;
    @CsvBindByPosition(position = 2)
    private String phoneNumber;
    @CsvBindByPosition(position = 3)
    private String openTime;
    @CsvBindByPosition(position = 4)
    private String detailLocation;
    @CsvBindByPosition(position = 5)
    private String gate;
    @CsvBindByPosition(position = 6)
    private String latitude;
    @CsvBindByPosition(position = 7)
    private String longitude;

    public Facility toEntity(Station station) {
        return Facility.of(
            String.join(" ", station.getName(), FacilityType.DISABLED_TOILET.getName()),
            FacilityType.DISABLED_TOILET,
            GeometryUtil.getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude)),
            detailLocation,
            String.join(";",
                    "개방시간:"+openTime,
                    "전화번호:"+phoneNumber,
                    "개찰구 내부/외부:"+gate)
        );
    }
}