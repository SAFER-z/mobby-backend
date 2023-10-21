package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;
import com.safer.safer.domain.Station;
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
                String.join(" ", station.getName(), number+"번", FacilityType.RAMP.getName()),
                FacilityType.RAMP,
                station.getCoordinate(),
                detailLocation
        );
    }
}
