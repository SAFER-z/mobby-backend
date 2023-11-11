package com.safer.safer.common.exception;

public class NoSuchElementException extends Exception {

    public NoSuchElementException(final ExceptionCode exceptionCode, final String element) {
        super(exceptionCode, element);
    }

    public NoSuchElementException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
