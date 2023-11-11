package com.safer.safer.exception;

public class JwtException extends AuthException {
    public JwtException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
