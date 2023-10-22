package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.util.CsvUtil;
import com.safer.safer.domain.util.GeometryUtil;
import com.safer.safer.domain.util.TMapUtil;
import com.safer.safer.domain.Station;
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
    private String roadAddress;

    public Station toEntity() {
        Point coordinate = latitude.isBlank() ? TMapUtil.findPointByAddress(roadAddress) :
                GeometryUtil.getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));

        return Station.of(
                CsvUtil.parseStationName(name),
                line,
                operator,
                roadAddress,
                coordinate
        );
    }
}
