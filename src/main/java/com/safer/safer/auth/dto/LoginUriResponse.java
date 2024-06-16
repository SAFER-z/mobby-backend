package com.safer.safer.auth.dto;

public record LoginUriResponse(
        String loginUri
) {
    public static LoginUriResponse of(String loginUri) {
        return new LoginUriResponse(loginUri);
    }
}
