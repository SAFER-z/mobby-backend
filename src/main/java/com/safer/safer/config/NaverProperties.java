package com.safer.safer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oauth2.naver")
public record NaverProperties(
        String clientId,
        String clientSecret,
        String responseType,
        String grantType,
        String state,
        String authorizationUri,
        String redirectUri,
        String tokenUri,
        String userInfoUri
) {
}
