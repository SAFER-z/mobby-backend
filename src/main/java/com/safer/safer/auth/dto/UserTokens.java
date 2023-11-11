package com.safer.safer.auth.dto;

public record UserTokens(
        String accessToken,
        String refreshToken
) {
    public static UserTokens of(String accessToken, String refreshToken) {
        return new UserTokens(accessToken, refreshToken);
    }
}
