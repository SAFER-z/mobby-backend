package com.safer.safer.batch.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private static final double LATITUDE_LOWER_BOUND = 33;
    private static final double LATITUDE_UPPER_BOUND = 43;
    private static final double LONGITUDE_LOWER_BOUND = 124;
    private static final double LONGITUDE_UPPER_BOUND = 132;

    public static Point getPoint(double latitude, double longitude) {
        return geometryFactory.createPoint(new Coordinate(latitude, longitude));
    }

    public static boolean isValidRange(double latitude, double longitude) {
        return latitude > LATITUDE_LOWER_BOUND && latitude < LATITUDE_UPPER_BOUND &&
                longitude > LONGITUDE_LOWER_BOUND && longitude < LONGITUDE_UPPER_BOUND;
    }
}
