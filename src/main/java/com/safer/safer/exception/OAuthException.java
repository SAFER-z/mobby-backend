package com.safer.safer.exception;

public class OAuthException extends Exception {

    public OAuthException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public OAuthException(final ExceptionCode exceptionCode, String message) {
        super(exceptionCode, message);
    }
}
