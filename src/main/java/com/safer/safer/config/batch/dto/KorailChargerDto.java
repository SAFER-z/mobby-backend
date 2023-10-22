package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;
import com.safer.safer.domain.Station;
import com.safer.safer.domain.util.CsvUtil;
import lombok.Getter;

@Getter
public class KorailChargerDto {
    @CsvBindByPosition(position = 0)
    private String line;
    @CsvBindByPosition(position = 1)
    private String stationName;
    @CsvBindByPosition(position = 2)
    private String detailLocation;
    @CsvBindByPosition(position = 3)
    private String phoneNumber;

    public Facility toEntity(Station station) {
        return Facility.of(
                CsvUtil.generateNameByStation(station, FacilityType.CHARGER),
                FacilityType.CHARGER,
                detailLocation,
                "전화번호:"+phoneNumber,
                station.getCoordinate()
        );
    }
}
