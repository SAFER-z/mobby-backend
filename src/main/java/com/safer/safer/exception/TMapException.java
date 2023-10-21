package com.safer.safer.exception;

public class TMapException extends Exception {

    public TMapException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
