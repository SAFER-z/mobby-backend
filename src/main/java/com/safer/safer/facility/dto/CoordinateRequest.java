package com.safer.safer.facility.dto;

import com.safer.safer.batch.util.GeometryUtil;
import org.locationtech.jts.geom.Point;

public record CoordinateRequest(
        double latitude,
        double longitude
) {
    public Point toPoint() {
        return GeometryUtil.toPoint(
                latitude,
                longitude
        );
    }

    public static CoordinateRequest of(double latitude, double longitude) {
        return new CoordinateRequest(latitude, longitude);
    }
}
