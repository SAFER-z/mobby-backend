package com.safer.safer.batch.dto.stationFacility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.station.domain.Station;
import com.safer.safer.batch.util.CsvUtil;
import lombok.Getter;

@Getter
public class StationElevatorDto {
    @CsvBindByPosition(position = 0)
    private String line;
    @CsvBindByPosition(position = 1)
    private String stationName;
    @CsvBindByPosition(position = 2)
    private String number;
    @CsvBindByPosition(position = 3)
    private String detailLocation;
    @CsvBindByPosition(position = 4)
    private String operatingRoute;


    public Facility toEntity(Station station) {
        return Facility.of(
                CsvUtil.generateNameByStation(station, FacilityType.ELEVATOR)
                        + " " + number.replaceAll("#", "") + "호기",
                FacilityType.ELEVATOR,
                detailLocation,
                "route="+operatingRoute,
                station.getCoordinate(),
                station
        );
    }
}
