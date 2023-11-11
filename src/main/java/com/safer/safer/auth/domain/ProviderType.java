package com.safer.safer.auth.domain;

import com.safer.safer.auth.exception.OAuthException;

import java.util.Arrays;

import static com.safer.safer.common.exception.ExceptionCode.UNSUPPORTED_OAUTH_TYPE;

public enum ProviderType {
    NAVER,
    KAKAO;

    public static ProviderType from(String type) {
        return Arrays.stream(values())
                .filter(providerType -> providerType.name().equals(type.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new OAuthException(UNSUPPORTED_OAUTH_TYPE, type));
    }
}
