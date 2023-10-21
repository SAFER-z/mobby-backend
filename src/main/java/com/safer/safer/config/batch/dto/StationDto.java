package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.util.GeometryUtil;
import com.safer.safer.domain.util.TMapUtil;
import com.safer.safer.domain.Station;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import static com.safer.safer.config.batch.tasklet.Constant.*;

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
    private String roadAddress;

    public Station toEntity() {
        Point coordinate = latitude.isEmpty() ? TMapUtil.findPointByAddress(roadAddress) :
                GeometryUtil.getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
        String stationName = name.replaceAll(REMOVAL_REGEX, "");

        return Station.of(
                stationName.endsWith("역") ? stationName : stationName.concat("역"),
                line,
                operator,
                roadAddress,
                coordinate
        );
    }
}
