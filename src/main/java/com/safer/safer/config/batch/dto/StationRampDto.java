package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;
import com.safer.safer.domain.Station;
import com.safer.safer.domain.util.CsvUtil;
import lombok.Getter;

@Getter
public class StationRampDto {
    @CsvBindByPosition(position = 0)
    private String line;
    @CsvBindByPosition(position = 1)
    private String stationName;
    @CsvBindByPosition(position = 2)
    private String number;
    @CsvBindByPosition(position = 3)
    private String detailLocation;

    public Facility toEntity(Station station) {
        return Facility.of(
                CsvUtil.generateNameByStation(station, number, FacilityType.ACCESSIBLE_RAMP),
                FacilityType.ACCESSIBLE_RAMP,
                station.getCoordinate(),
                detailLocation,
                station
        );
    }
}
