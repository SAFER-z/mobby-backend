package com.safer.safer.common.exception;

public class ForbiddenException extends Exception {
    public ForbiddenException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
