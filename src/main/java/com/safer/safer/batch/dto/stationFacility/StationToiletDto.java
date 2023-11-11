package com.safer.safer.batch.dto.stationFacility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.station.domain.Station;
import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.util.GeometryUtil;
import lombok.Getter;

@Getter
public class StationToiletDto {
    @CsvBindByPosition(position = 0)
    private String line;
    @CsvBindByPosition(position = 1)
    private String stationName;
    @CsvBindByPosition(position = 2)
    private String phoneNumber;
    @CsvBindByPosition(position = 3)
    private String openTime;
    @CsvBindByPosition(position = 4)
    private String detailLocation;
    @CsvBindByPosition(position = 5)
    private String gate;
    @CsvBindByPosition(position = 6)
    private String latitude;
    @CsvBindByPosition(position = 7)
    private String longitude;

    public Facility toEntity(Station station) {
        return Facility.of(
                CsvUtil.generateNameByStation(station, FacilityType.ACCESSIBLE_TOILET),
                FacilityType.ACCESSIBLE_TOILET,
                detailLocation,
                String.join(";",
                        "weekdayOpeningHours="+openTime,
                        "phoneNumber="+phoneNumber,
                        "turnstile="+gate
                ),
                GeometryUtil.getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude)),
                station
        );
    }
}
