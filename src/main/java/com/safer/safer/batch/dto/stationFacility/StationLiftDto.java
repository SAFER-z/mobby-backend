package com.safer.safer.batch.dto.stationFacility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.station.domain.Station;
import com.safer.safer.batch.util.CsvUtil;
import lombok.Getter;

import static com.safer.safer.facility.domain.FacilityType.WHEELCHAIR_LIFT;

@Getter
public class StationLiftDto {
    @CsvBindByPosition(position = 0)
    private String operatorType;
    @CsvBindByPosition(position = 1)
    private String line;
    @CsvBindByPosition(position = 2)
    private String stationName;
    @CsvBindByPosition(position = 3)
    private String start;
    @CsvBindByPosition(position = 4)
    private String startFloor;
    @CsvBindByPosition(position = 5)
    private String startDetailLocation;
    @CsvBindByPosition(position = 6)
    private String end;
    @CsvBindByPosition(position = 7)
    private String endFloor;
    @CsvBindByPosition(position = 8)
    private String endDetailLocation;


    public Facility toEntity(Station station) {
        String detailLocation = endDetailLocation.isBlank() ? startDetailLocation :
                startDetailLocation + " - " + endDetailLocation;

        return Facility.of(
                CsvUtil.generateNameByStation(station, WHEELCHAIR_LIFT.getName()),
                WHEELCHAIR_LIFT,
                station.getCoordinate(),
                String.join(";",
                        "detailLocation="+detailLocation,
                        "route="+start+startFloor+"층-"+end+endFloor+"층"
                ),
                station
        );
    }
}
