package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;
import com.safer.safer.domain.Station;
import com.safer.safer.domain.util.CsvUtil;
import lombok.Getter;

@Getter
public class KorailToiletDto {
    @CsvBindByPosition(position = 0)
    private String line;
    @CsvBindByPosition(position = 1)
    private String stationName;
    @CsvBindByPosition(position = 2)
    private String gate;
    @CsvBindByPosition(position = 3)
    private String detailLocation;

    public Facility toEntity(Station station) {
        return Facility.of(
                CsvUtil.generateNameByStation(station, FacilityType.ACCESSIBLE_TOILET),
                FacilityType.ACCESSIBLE_TOILET,
                detailLocation,
                "turnstile="+gate,
                station.getCoordinate(),
                station
        );
    }
}
