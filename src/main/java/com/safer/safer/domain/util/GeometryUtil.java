package com.safer.safer.domain.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public static Point getPoint(double latitude, double longitude) {
        return geometryFactory.createPoint(new Coordinate(latitude, longitude));
    }
}
