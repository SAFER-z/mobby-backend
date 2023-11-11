package com.safer.safer.auth.application;

import com.safer.safer.auth.domain.OAuthUserInfo;
import com.safer.safer.auth.domain.ProviderType;
import org.springframework.web.client.RestTemplate;

public interface OauthProvider {

    RestTemplate restTemplate = new RestTemplate();

    ProviderType getType();

    OAuthUserInfo getUserInfo(String code);

    String requestAccessToken(String code);

    String getLoginRedirectUri();
}
