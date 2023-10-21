package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;
import com.safer.safer.domain.Station;
import lombok.Getter;

@Getter
public class KorailToiletDto {
    @CsvBindByPosition(position = 0)
    private String stationName;
    @CsvBindByPosition(position = 1)
    private String detailLocation;

    public Facility toEntity(Station station) {
        return Facility.of(
                String.join(" ", station.getName(), FacilityType.DISABLED_TOILET.getName()),
                FacilityType.DISABLED_TOILET,
                station.getCoordinate(),
                detailLocation
        );
    }
}
