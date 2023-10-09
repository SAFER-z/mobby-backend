package com.safer.safer.batch;

import com.safer.safer.exception.TMapException;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class TMapUtil {

    private static String appKey;

    @Value("${appKey}")
    private void setAppKey(String value) {
        TMapUtil.appKey = value;
    }

    public static Point findPointByAddress(String address) throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = createUri(address);

        RequestEntity<Void> request = RequestEntity
                .get(uri)
                .header("appKey", TMapUtil.appKey)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        if(!response.getStatusCode().equals(HttpStatus.OK))
            throw new TMapException();

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

    private static Point getCoordinate(ResponseEntity<String> response) throws ParseException {
        String[] body = response.getBody().split(",");
        String latitude = body[11].split(":")[1].replaceAll("\"", "");
        String longitude = body[12].split(":")[1].replaceAll("\"", "");

        String pointWKT = String.format("POINT(%s %s)", latitude, longitude);

        return (Point) new WKTReader().read(pointWKT);
    }
}
