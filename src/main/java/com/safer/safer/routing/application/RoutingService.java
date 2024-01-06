package com.safer.safer.routing.application;

import com.safer.safer.facility.application.FacilityService;
import com.safer.safer.facility.dto.CoordinateRequest;
import com.safer.safer.facility.dto.FacilityDistanceResponse;
import com.safer.safer.routing.dto.SearchResponse;
import com.safer.safer.routing.dto.address.AddressResponse;
import com.safer.safer.routing.infrastructure.address.AddressRequester;
import com.safer.safer.station.application.StationService;
import com.safer.safer.station.dto.StationDistanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.safer.safer.batch.util.BatchConstant.NUMBER_REGEX;

@Service
@RequiredArgsConstructor
public class RoutingService {

    private final AddressRequester addressRequester;
    private final FacilityService facilityService;
    private final StationService stationService;

    public SearchResponse searchByKeyword(String keyword, CoordinateRequest coordinate) {

        List<AddressResponse> addressSearchResult = isValidKeywordForAddress(keyword) ?
                addressRequester.requestAddress(keyword) : List.of();
        List<FacilityDistanceResponse> facilitySearchResult = facilityService.searchFacilities(keyword, coordinate);
        List<StationDistanceResponse> stationSearchResult = stationService.searchStations(keyword, coordinate);

        return SearchResponse.of(facilitySearchResult, stationSearchResult, addressSearchResult);
    }

    private boolean isValidKeywordForAddress(String keyword) {
        return !keyword.matches(NUMBER_REGEX) && keyword.length() > 1;
    }
}
