package com.safer.safer.batch.dto.stationFacility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.station.domain.Station;
import com.safer.safer.batch.util.CsvUtil;
import lombok.Getter;

@Getter
public class StationChargerDto {
    @CsvBindByPosition(position = 0)
    private String operatorType;
    @CsvBindByPosition(position = 1)
    private String line;
    @CsvBindByPosition(position = 2)
    private String stationName;
    @CsvBindByPosition(position = 3)
    private String detailLocation;

    public Facility toEntity(Station station) {
        return Facility.of(
                CsvUtil.generateNameByStation(station, FacilityType.WHEELCHAIR_CHARGER),
                FacilityType.WHEELCHAIR_CHARGER,
                station.getCoordinate(),
                "detailLocation="+detailLocation,
                station
        );
    }
}
