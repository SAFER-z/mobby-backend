package com.safer.safer.routing.dto.tmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.safer.safer.batch.util.GeometryUtil;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoordinateResponse {

    @JsonProperty("searchPoiInfo")
    private SearchResult.SearchPlaceResult searchPlaceResult;

    public Point getCoordinate() {
        return GeometryUtil.toPoint(
                searchPlaceResult.pois.poi.get(0).getFrontLat(),
                searchPlaceResult.pois.poi.get(0).getFrontLon()
        );
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class SearchPoiInfo {
        List<SearchResult.Place> pois;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class POI {
        private double frontLat;
        private double frontLon;
    }
}
