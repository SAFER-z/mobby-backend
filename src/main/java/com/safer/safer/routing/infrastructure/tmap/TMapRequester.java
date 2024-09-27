package com.safer.safer.routing.infrastructure.tmap;

import com.safer.safer.batch.exception.TMapException;
import com.safer.safer.routing.dto.tmap.CoordinateResponse;
import com.safer.safer.routing.dto.tmap.SearchDetailResult;
import com.safer.safer.routing.dto.tmap.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static com.safer.safer.common.exception.ExceptionCode.FAIL_TO_REQUEST_TMAP_API;
import static com.safer.safer.routing.dto.tmap.SearchResult.Place;

@Component
@RequiredArgsConstructor
@Slf4j
public class TMapRequester {

    private final WebClient webClient;
    private static final String BASE_URL = "https://apis.openapi.sk.com/";


    public List<Place> searchWithCoordinate(String keyword, double lat, double lon) {
        URI uri = createUri(keyword, lat, lon);

        Mono<SearchResult> responseMono = webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new TMapException(FAIL_TO_REQUEST_TMAP_API, keyword + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new TMapException(FAIL_TO_REQUEST_TMAP_API, keyword + response.statusCode())))
                .bodyToMono(SearchResult.class);

        return Optional.ofNullable(responseMono.block())
                .orElseThrow(() -> new TMapException(FAIL_TO_REQUEST_TMAP_API, keyword))
                .getSearchPoiInfo().getPois().getPoi();
    }

    public Mono<Point> searchCoordinate(String keyword) {
        return webClient.get()
                .uri(createUri(keyword))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new TMapException(FAIL_TO_REQUEST_TMAP_API, keyword + " " + response.statusCode()))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new TMapException(FAIL_TO_REQUEST_TMAP_API, keyword + " " + response.statusCode()))
                )
                .bodyToMono(CoordinateResponse.class)
                .map(CoordinateResponse::getCoordinate)
                .retryWhen(Retry.backoff(10, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof TMapException)
                        .doBeforeRetry(retrySignal -> log.warn("{} 요청 실패 : 재시도 {}회", keyword, retrySignal.totalRetries())))
                .doOnError(e -> log.warn("재시도가 실패했습니다."));

    }

    public SearchDetailResult searchPlace(String placeId) {
        URI uri = createUriForPlaceDetail(placeId);

        Mono<SearchDetailResult> responseMono = webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new TMapException(FAIL_TO_REQUEST_TMAP_API, placeId + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new TMapException(FAIL_TO_REQUEST_TMAP_API, placeId + response.statusCode())))
                .bodyToMono(SearchDetailResult.class);

        return Optional.ofNullable(responseMono.block())
                .orElseThrow(() -> new TMapException(FAIL_TO_REQUEST_TMAP_API, placeId));
    }

    private URI createUri(String keyword) {
        return UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path("tmap/pois")
                .queryParam("searchKeyword", keyword)
                .queryParam("count", 1)
                .encode()
                .build()
                .toUri();
    }

    private URI createUri(String keyword, double lat, double lon) {
        return UriComponentsBuilder
                .fromUriString(BASE_URL)
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
                .fromUriString(BASE_URL)
                .path("tmap/pois/" + placeId)
                .encode()
                .build()
                .toUri();
    }
}
