package com.safer.safer.routing.application;

import com.safer.safer.facility.application.FacilityService;
import com.safer.safer.facility.dto.CoordinateRequest;
import com.safer.safer.facility.dto.FacilityDistanceResponse;
import com.safer.safer.routing.dto.SearchResponse;
import com.safer.safer.routing.dto.tmap.TMapResponse;
import com.safer.safer.routing.infrastructure.tmap.TMapRequester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.safer.safer.routing.dto.tmap.POIResponse.POI;

@Service
@RequiredArgsConstructor
public class RoutingService {

    private final FacilityService facilityService;
    private final TMapRequester tMapRequester;

    public SearchResponse searchByKeyword(String keyword, double latitude, double longitude) {
        CoordinateRequest coordinate = CoordinateRequest.of(latitude, longitude);

        List<POI> tMapSearchResult = tMapRequester.searchWithCoordinate(keyword, latitude, longitude);
        List<FacilityDistanceResponse> facilitySearchResult = facilityService.searchFacilities(keyword, coordinate);

        return SearchResponse.of(
                facilitySearchResult,
                tMapSearchResult.stream()
                        .map(TMapResponse::from)
                        .toList()
        );
    }
}
