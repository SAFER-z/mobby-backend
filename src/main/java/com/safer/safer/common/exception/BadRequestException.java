package com.safer.safer.common.exception;

public class BadRequestException extends Exception {
    public BadRequestException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
