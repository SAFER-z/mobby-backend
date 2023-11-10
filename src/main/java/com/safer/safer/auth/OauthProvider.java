package com.safer.safer.auth;

import org.springframework.web.client.RestTemplate;

public interface OauthProvider {

    RestTemplate restTemplate = new RestTemplate();

    ProviderType getType();

    OAuthUserInfo getUserInfo(String code);

    String requestAccessToken(String code);

    String getLoginRedirectUri();
}
