package com.safer.safer.domain.util;

import com.safer.safer.exception.TMapException;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

import static com.safer.safer.exception.ExceptionCode.FAIL_TO_REQUEST_TMAP_API;

@Component
public class TMapUtil {

    private static String appKey;

    @Value("${appKey}")
    private void setAppKey(String value) {
        appKey = value;
    }

    public static Point findPointByKeyword(String keyword) {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = createUri(keyword);

        RequestEntity<Void> request = RequestEntity
                .get(uri)
                .header("appKey", TMapUtil.appKey)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        if(!response.getStatusCode().equals(HttpStatus.OK))
            throw new TMapException(FAIL_TO_REQUEST_TMAP_API, response.getStatusCode().toString() + " 검색어:"+keyword);

        return getCoordinate(response);
    }

    private static URI createUri(String searchKeyword) {
        return  UriComponentsBuilder
                .fromUriString("https://apis.openapi.sk.com/")
                .path("tmap/pois")
                .queryParam("searchKeyword", searchKeyword)
                .queryParam("count", 1)
                .encode()
                .build()
                .toUri();
    }

    private static Point getCoordinate(ResponseEntity<String> response) {
        String[] body = Objects.requireNonNull(response.getBody()).split(",");
        double latitude = Double.parseDouble(body[11].split(":")[1].replaceAll("\"", ""));
        double longitude = Double.parseDouble(body[12].split(":")[1].replaceAll("\"", ""));

        return GeometryUtil.getPoint(latitude, longitude);
    }
}
