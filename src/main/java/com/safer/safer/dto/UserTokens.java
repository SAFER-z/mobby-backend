package com.safer.safer.dto;

public record UserTokens(
        String accessToken,
        String refreshToken
) {
    public static UserTokens of(String accessToken, String refreshToken) {
        return new UserTokens(accessToken, refreshToken);
    }
}
