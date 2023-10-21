package com.safer.safer.exception;

public class NoSuchElementException extends Exception {

    public NoSuchElementException(final ExceptionCode exceptionCode, final String element) {
        super(exceptionCode, element);
    }
}
