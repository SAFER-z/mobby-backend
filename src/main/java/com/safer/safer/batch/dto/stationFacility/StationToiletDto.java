package com.safer.safer.batch.dto.stationFacility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.station.domain.Station;
import com.safer.safer.batch.util.CsvUtil;
import lombok.Getter;

import static com.safer.safer.batch.util.BatchConstant.ACCESSIBLE_TOILET;
import static com.safer.safer.batch.util.BatchConstant.NORMAL_TOILET;
import static com.safer.safer.facility.domain.FacilityType.TOILET;

@Getter
public class StationToiletDto {
    @CsvBindByPosition(position = 0)
    private String operatorType;
    @CsvBindByPosition(position = 1)
    private String line;
    @CsvBindByPosition(position = 2)
    private String stationName;
    @CsvBindByPosition(position = 3)
    private String turnstile;
    @CsvBindByPosition(position = 4)
    private String detailLocation;
    @CsvBindByPosition(position = 5)
    private String accessibility;
    @CsvBindByPosition(position = 6)
    private String openingHours;

    public Facility toEntity(Station station) {
        String toiletType = accessibility.equals("일반") ? NORMAL_TOILET : ACCESSIBLE_TOILET;
        return Facility.of(
                CsvUtil.generateNameByStation(station, toiletType),
                TOILET,
                station.getCoordinate(),
                String.join(";",
                        "detailLocation="+detailLocation,
                        "openingHours="+openingHours,
                        "turnstile="+turnstile,
                        "accessible="+(accessibility.equals("일반")?"no":"yes")
                ),
                station
        );
    }
}
