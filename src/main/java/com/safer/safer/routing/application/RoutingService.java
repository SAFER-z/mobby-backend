package com.safer.safer.routing.application;

import com.safer.safer.batch.util.GeometryUtil;
import com.safer.safer.facility.application.FacilityService;
import com.safer.safer.facility.dto.FacilityDistanceResponse;
import com.safer.safer.routing.dto.SearchResponse;
import com.safer.safer.routing.dto.tmap.PlaceDetailResponse;
import com.safer.safer.routing.dto.tmap.PlaceResponse;
import com.safer.safer.routing.infrastructure.tmap.TMapRequester;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.safer.safer.routing.dto.tmap.SearchResult.Place;

@Service
@RequiredArgsConstructor
public class RoutingService {

    private final FacilityService facilityService;
    private final TMapRequester tMapRequester;

    public SearchResponse searchByKeyword(String keyword, double latitude, double longitude) {
        Point coordinate = GeometryUtil.toPoint(latitude, longitude);

        List<Place> tMapSearchResult = tMapRequester.searchWithCoordinate(keyword, latitude, longitude);
        List<FacilityDistanceResponse> facilitySearchResult = facilityService.searchFacilities(keyword, coordinate);

        return SearchResponse.of(
                facilitySearchResult,
                tMapSearchResult.stream()
                        .map(PlaceResponse::from)
                        .toList()
        );
    }

    public PlaceDetailResponse findPlace(String placeId) {
        return PlaceDetailResponse.from(tMapRequester.searchPlace(placeId));
    }
}
