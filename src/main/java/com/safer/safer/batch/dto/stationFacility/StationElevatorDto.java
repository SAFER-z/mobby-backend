package com.safer.safer.batch.dto.stationFacility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.station.domain.Station;
import com.safer.safer.batch.util.CsvUtil;
import lombok.Getter;

import static com.safer.safer.facility.domain.FacilityType.ELEVATOR;

@Getter
public class StationElevatorDto {
    @CsvBindByPosition(position = 0)
    private String operatorType;
    @CsvBindByPosition(position = 1)
    private String line;
    @CsvBindByPosition(position = 2)
    private String stationName;
    @CsvBindByPosition(position = 3)
    private String number;
    @CsvBindByPosition(position = 4)
    private String detailLocation;
    @CsvBindByPosition(position = 5)
    private String start;
    @CsvBindByPosition(position = 6)
    private String startFloor;
    @CsvBindByPosition(position = 7)
    private String end;
    @CsvBindByPosition(position = 8)
    private String endFloor;


    public Facility toEntity(Station station) {
        return Facility.of(
                CsvUtil.generateNameByStation(station, ELEVATOR.getName())
                        + " " + number + "호기",
                ELEVATOR,
                station.getCoordinate(),
                String.join(";",
                        "detailLocation="+detailLocation,
                        "route="+start+startFloor+"층-"+end+endFloor+"층"
                ),
                station
        );
    }
}
