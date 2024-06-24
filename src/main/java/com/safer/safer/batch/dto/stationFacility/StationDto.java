package com.safer.safer.batch.dto.stationFacility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.util.GeometryUtil;
import com.safer.safer.station.domain.OperatorType;
import com.safer.safer.station.domain.Station;
import com.safer.safer.station.domain.StationKey;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Getter
public class StationDto {
    @CsvBindByPosition(position = 0)
    private String name;
    @CsvBindByPosition(position = 1)
    private String line;
    @CsvBindByPosition(position = 2)
    private String latitude;
    @CsvBindByPosition(position = 3)
    private String longitude;
    @CsvBindByPosition(position = 4)
    private String operator;
    @CsvBindByPosition(position = 5)
    private String address;
    @CsvBindByPosition(position = 6)
    private String phoneNumber;
    @CsvBindByPosition(position = 7)
    private String accessibleRamp;
    @CsvBindByPosition(position = 8)
    private String accessibleArea;


    public Station toEntity(Point coordinate) {
        OperatorType operatorType = OperatorType.from(operator);
        String stationName = CsvUtil.parseStationName(name);

        return Station.of(
                StationKey.of(
                        stationName,
                        line,
                        operatorType.name()
                ),
                address,
                needsCoordinate() ? coordinate : GeometryUtil.toPoint(
                        Double.parseDouble(latitude),
                        Double.parseDouble(longitude)
                ),
                phoneNumber,
                accessibleRamp.equals("Y"),
                accessibleArea
        );
    }

    public boolean needsCoordinate() {
        return latitude.isBlank();
    }
}
