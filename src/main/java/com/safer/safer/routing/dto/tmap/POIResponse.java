package com.safer.safer.routing.dto.tmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class POIResponse {

    @JsonProperty("searchPoiInfo")
    private SearchPoiInfo searchPoiInfo;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchPoiInfo {
        PoiList pois;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PoiList {
        List<POI> poi;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class POI {
        private String id;
        private String name;
        private String telNo;
        private double frontLat;
        private double frontLon;
        private String detailAddrName;
        private double radius;
        private NewAddress newAddressList;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class NewAddress {
        private List<Address> newAddress;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Address {
        private String fullAddressRoad;
    }
}
