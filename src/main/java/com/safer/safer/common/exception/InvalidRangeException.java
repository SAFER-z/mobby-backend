package com.safer.safer.common.exception;

public class InvalidRangeException extends Exception {
    public InvalidRangeException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
