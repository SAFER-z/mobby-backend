package com.safer.safer.auth;

import com.safer.safer.config.KakaoProperties;
import com.safer.safer.exception.OAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static com.safer.safer.exception.ExceptionCode.FAIL_TO_REQUEST_TOKEN;
import static com.safer.safer.exception.ExceptionCode.FAIL_TO_REQUEST_USER_INFO;

@Component
@RequiredArgsConstructor
public class KakaoOauthProvider implements OauthProvider {

    private final KakaoProperties properties;

    @Override
    public ProviderType getType() {
        return ProviderType.KAKAO;
    }

    @Override
    public OAuthUserInfo getUserInfo(final String code) {
        final String accessToken = requestAccessToken(code);
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        return fetchUserInfo(request);
    }

    private OAuthUserInfo fetchUserInfo(HttpEntity<MultiValueMap<String, String>> request) {
        final ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                properties.userInfoUri(),
                HttpMethod.GET,
                request,
                KakaoUserInfo.class
        );

        if(!response.getStatusCode().is2xxSuccessful())
            throw new OAuthException(FAIL_TO_REQUEST_USER_INFO);

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new OAuthException(FAIL_TO_REQUEST_USER_INFO));
    }

    @Override
    public String requestAccessToken(String code) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", properties.grantType());
        body.add("client_id", properties.clientId());
        body.add("client_secret", properties.clientSecret());
        body.add("redirect_uri", properties.redirectUri());
        body.add("code", code);

        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        return fetchAccessToken(request);
    }

    private String fetchAccessToken(final HttpEntity<MultiValueMap<String, String>> request) {
        final ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
                    properties.tokenUri(),
                    request,
                    KakaoTokenResponse.class
        );

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new OAuthException(FAIL_TO_REQUEST_TOKEN))
                .getAccessToken();
    }

    @Override
    public String getLoginRedirectUri() {
        return UriComponentsBuilder.fromUriString(properties.authorizationUri())
                .queryParam("client_id", properties.clientId())
                .queryParam("redirect_uri", properties.redirectUri())
                .queryParam("response_type", properties.responseType())
                .build().toUriString();
    }
}
