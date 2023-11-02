package com.safer.safer.dto;

import com.safer.safer.domain.util.GeometryUtil;
import org.locationtech.jts.geom.Point;

public record CoordinateRequest(
        double latitude,
        double longitude
) {
    public Point toPoint() {
        return GeometryUtil.getPoint(
                latitude,
                longitude
        );
    }
}
