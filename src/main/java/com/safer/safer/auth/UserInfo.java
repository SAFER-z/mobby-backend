package com.safer.safer.auth;

public record UserInfo(
        Long userId
) {
    public static UserInfo of(final Long userId) {
        return new UserInfo(userId);
    }
}
