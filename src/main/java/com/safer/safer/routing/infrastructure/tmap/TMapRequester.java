package com.safer.safer.routing.infrastructure.tmap;

import com.safer.safer.batch.exception.TMapException;
import com.safer.safer.routing.dto.tmap.CoordinateResponse;
import com.safer.safer.routing.dto.tmap.SearchDetailResult;
import com.safer.safer.routing.dto.tmap.SearchResult;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.safer.safer.common.exception.ExceptionCode.FAIL_TO_REQUEST_TMAP_API;
import static com.safer.safer.routing.dto.tmap.SearchResult.Place;

@Component
@RequiredArgsConstructor
public class TMapRequester {

    private static String appKey;
    private final RestTemplate restTemplate;
    private static final String BASE_URI = "https://apis.openapi.sk.com/";

    @Value("${appKey}")
    private void setAppKey(String value) {
        appKey = value;
    }

    public List<Place> searchWithCoordinate(String keyword, double lat, double lon) {
        URI uri = createUri(keyword, lat, lon);
        RequestEntity<Void> request = createRequest(uri);

        ResponseEntity<SearchResult> response = restTemplate.exchange(
                request,
                SearchResult.class
        );

        if(!response.getStatusCode().is2xxSuccessful())
            throw new TMapException(FAIL_TO_REQUEST_TMAP_API, response.getStatusCode() + " 검색어:"+keyword);

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new TMapException(FAIL_TO_REQUEST_TMAP_API, response.getStatusCode().toString()))
                .getSearchPoiInfo().getPois().getPoi();
    }

    public Point searchCoordinate(String keyword) {
        URI uri = createUri(keyword);
        RequestEntity<Void> request = createRequest(uri);

        ResponseEntity<CoordinateResponse> response = restTemplate.exchange(
                request,
                CoordinateResponse.class
        );

        if(!response.getStatusCode().is2xxSuccessful())
            throw new TMapException(FAIL_TO_REQUEST_TMAP_API, response.getStatusCode() + " 검색어:"+keyword);

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new TMapException(FAIL_TO_REQUEST_TMAP_API, response.getStatusCode().toString()))
                .getCoordinate();
    }

    public SearchDetailResult searchPlace(String placeId) {
        URI uri = createUriForPlaceDetail(placeId);
        RequestEntity<Void> request = createRequest(uri);

        ResponseEntity<SearchDetailResult> response = restTemplate.exchange(
                request,
                SearchDetailResult.class
        );

        if(!response.getStatusCode().is2xxSuccessful())
            throw new TMapException(FAIL_TO_REQUEST_TMAP_API, response.getStatusCode().toString());

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new TMapException(FAIL_TO_REQUEST_TMAP_API, response.getStatusCode().toString()));
    }

    private URI createUri(String keyword) {
        return UriComponentsBuilder
                .fromUriString(BASE_URI)
                .path("tmap/pois")
                .queryParam("searchKeyword", keyword)
                .queryParam("count", 1)
                .encode()
                .build()
                .toUri();
    }

    private URI createUri(String keyword, double lat, double lon) {
        return UriComponentsBuilder
                .fromUriString(BASE_URI)
                .path("tmap/pois")
                .queryParam("searchKeyword", keyword)
                .queryParam("centerLat", lat)
                .queryParam("centerLon", lon)
                .queryParam("radius", 0)
                .queryParam("count", 10)
                .encode()
                .build()
                .toUri();
    }

    private URI createUriForPlaceDetail(String placeId) {
        return UriComponentsBuilder
                .fromUriString(BASE_URI)
                .path("/tmap/pois/" + placeId)
                .encode()
                .build()
                .toUri();
    }

    private RequestEntity<Void> createRequest(URI uri) {
        return RequestEntity
                .get(uri)
                .header("appKey", TMapRequester.appKey)
                .build();
    }
}
