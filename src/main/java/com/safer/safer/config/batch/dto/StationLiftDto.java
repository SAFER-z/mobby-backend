package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;
import com.safer.safer.domain.Station;
import lombok.Getter;

@Getter
public class StationLiftDto {
    @CsvBindByPosition(position = 0)
    private String line;
    @CsvBindByPosition(position = 1)
    private String stationName;
    @CsvBindByPosition(position = 2)
    private String number;
    @CsvBindByPosition(position = 3)
    private String operatingRoute;
    @CsvBindByPosition(position = 4)
    private String detailLocation;

    public Facility toEntity(Station station) {
        return Facility.of(
                String.join(" ", station.getName(), FacilityType.LIFT.getName()),
                FacilityType.LIFT,
                station.getCoordinate(),
                detailLocation,
                String.join(";",
                        "호기:"+number.replaceAll("#", " "),
                        "운행구간:"+operatingRoute)
        );
    }
}
