package com.safer.safer.auth.infrastructure.naver;

import com.safer.safer.auth.domain.OAuthUserInfo;
import com.safer.safer.auth.application.OauthProvider;
import com.safer.safer.auth.domain.ProviderType;
import com.safer.safer.auth.infrastructure.naver.dto.NaverTokenResponse;
import com.safer.safer.auth.infrastructure.naver.dto.NaverUserInfo;
import com.safer.safer.auth.exception.OAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static com.safer.safer.common.exception.ExceptionCode.FAIL_TO_REQUEST_TOKEN;
import static com.safer.safer.common.exception.ExceptionCode.FAIL_TO_REQUEST_USER_INFO;

@Component
@RequiredArgsConstructor
public class NaverOauthProvider implements OauthProvider {

    private final NaverProperties properties;

    @Override
    public ProviderType getType() {
        return ProviderType.NAVER;
    }

    @Override
    public OAuthUserInfo getUserInfo(final String code) {
        String accessToken = requestAccessToken(code);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        return fetchUserInfo(request);
    }

    private OAuthUserInfo fetchUserInfo(HttpEntity<MultiValueMap<String, String>> request) {
        final ResponseEntity<NaverUserInfo> response = restTemplate.exchange(
                properties.userInfoUri(),
                HttpMethod.GET,
                request,
                NaverUserInfo.class
        );

        if(!response.getStatusCode().is2xxSuccessful())
            throw new OAuthException(FAIL_TO_REQUEST_USER_INFO);

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new OAuthException(FAIL_TO_REQUEST_USER_INFO));
    }

    @Override
    public String requestAccessToken(final String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", properties.grantType());
        body.add("client_id", properties.clientId());
        body.add("client_secret", properties.clientSecret());
        body.add("code", code);
        body.add("state", properties.state());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        return fetchAccessToken(request);
    }

    private String fetchAccessToken(HttpEntity<MultiValueMap<String, String>> request) {
        final ResponseEntity<NaverTokenResponse> response = restTemplate.postForEntity(
                properties.tokenUri(),
                request,
                NaverTokenResponse.class
        );

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new OAuthException(FAIL_TO_REQUEST_TOKEN))
                .getAccessToken();
    }

    @Override
    public String getLoginRedirectUri() {
        return UriComponentsBuilder.fromUriString(properties.authorizationUri())
                .queryParam("response_type", properties.responseType())
                .queryParam("client_id", properties.clientId())
                .queryParam("redirect_uri", properties.redirectUri())
                .queryParam("state", properties.state())
                .build().toUriString();
    }
}
