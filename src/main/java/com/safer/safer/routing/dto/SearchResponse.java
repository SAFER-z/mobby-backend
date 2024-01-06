package com.safer.safer.routing.dto;

import com.safer.safer.facility.dto.FacilityDistanceResponse;
import com.safer.safer.routing.dto.address.AddressResponse;
import com.safer.safer.station.dto.StationDistanceResponse;

import java.util.List;

public record SearchResponse(
        List<StationDistanceResponse> stations,
        List<FacilityDistanceResponse> facilities,
        List<AddressResponse> addresses
) {
    public static SearchResponse of(List<FacilityDistanceResponse> facilities,
                                    List<StationDistanceResponse> stations,
                                    List<AddressResponse> addresses
    ) {
        return new SearchResponse(stations, facilities, addresses);
    }
}
