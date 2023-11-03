package com.safer.safer.dto;

import com.safer.safer.exception.Exception;

public record ErrorResponse(
        String message
) {
    public static ErrorResponse from(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
