package com.safer.safer.routing.infrastructure.address;

import com.safer.safer.routing.dto.address.AddressResponse;
import com.safer.safer.routing.dto.address.AddressResultDto;
import com.safer.safer.routing.exception.AddressException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.safer.safer.common.exception.ExceptionCode.FAIL_TO_REQUEST_JUSO;
import static com.safer.safer.common.exception.ExceptionCode.INVALID_JUSO_REQUEST;

@Component
@RequiredArgsConstructor
public class AddressRequester {

    @Value("${confirmKey}")
    private String confirmKey;
    private final RestTemplate restTemplate;
    private static final String SUCCESS = "0";

    public List<AddressResponse> requestAddress(String keyword) {
        URI uri = createUri(keyword);
        final ResponseEntity<AddressResultDto> response = restTemplate.exchange(
                new RequestEntity<>(null, HttpMethod.GET, uri), AddressResultDto.class
        );

        AddressResultDto result = Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new AddressException(FAIL_TO_REQUEST_JUSO));

        if(!response.getStatusCode().is2xxSuccessful() || !result.getErrorCode().equals(SUCCESS)) {
            throw new AddressException(INVALID_JUSO_REQUEST, result.getErrorMessage());
        }

        return result.getAddressList().stream()
                .map(AddressResponse::of)
                .toList();
    }

    private URI createUri(String keyword) {
        return UriComponentsBuilder
                .fromUriString("https://business.juso.go.kr/")
                .path("addrlink/addrLinkApi.do")
                .queryParam("confmKey", confirmKey)
                .queryParam("currentPage", 1)
                .queryParam("countPerPage", 5)
                .queryParam("keyword", keyword)
                .queryParam("resultType", "json")
                .queryParam("firstSort", "none")
                .encode()
                .build()
                .toUri();
    }
}
