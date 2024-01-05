package com.safer.safer.routing.application;

import com.safer.safer.facility.application.FacilityService;
import com.safer.safer.facility.dto.CoordinateRequest;
import com.safer.safer.facility.dto.FacilitiesDistanceResponse;
import com.safer.safer.facility.dto.FacilityDistanceResponse;
import com.safer.safer.routing.dto.SearchResponse;
import com.safer.safer.routing.dto.address.AddressResponse;
import com.safer.safer.routing.infrastructure.address.AddressRequester;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutingService {

    private final AddressRequester addressRequester;
    private final FacilityService facilityService;

    public SearchResponse searchByKeyword(String keyword, CoordinateRequest coordinate) {
        List<AddressResponse> addressSearchResult = addressRequester.requestAddress(keyword);
        List<FacilityDistanceResponse> facilitySearchResult = facilityService.searchFacilities(keyword, coordinate);

        return SearchResponse.of(facilitySearchResult, addressSearchResult);
    }
}
