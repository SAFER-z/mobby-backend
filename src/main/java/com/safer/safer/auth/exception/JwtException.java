package com.safer.safer.auth.exception;

import com.safer.safer.common.exception.ExceptionCode;

public class JwtException extends AuthException {
    public JwtException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
