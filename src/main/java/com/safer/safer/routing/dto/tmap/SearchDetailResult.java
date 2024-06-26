package com.safer.safer.routing.dto.tmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchDetailResult {

    @JsonProperty("poiDetailInfo")
    private PlaceDetail poiDetailInfo;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlaceDetail {
        private String name;
        private String bizCatName;
        private String tel;
        private String bldAddr;
        private String bldNo1;
        private String bldNo2;
        private int parkFlag;
        private String routeInfo;
        private String desc;
        private String additionalInfo;
        private String useTime;
    }
}
