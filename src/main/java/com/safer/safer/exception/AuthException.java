package com.safer.safer.exception;

public class AuthException extends Exception {
    public AuthException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
