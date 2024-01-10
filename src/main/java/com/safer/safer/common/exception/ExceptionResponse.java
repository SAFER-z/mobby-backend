package com.safer.safer.common.exception;

public record ExceptionResponse(
        String message
) {
    public static ExceptionResponse from(final Exception exception) {
        return new ExceptionResponse(exception.getMessage());
    }

    public static ExceptionResponse of(String message) {
        return new ExceptionResponse(message);
    }
}
