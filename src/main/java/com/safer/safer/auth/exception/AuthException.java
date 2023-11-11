package com.safer.safer.auth.exception;

import com.safer.safer.common.exception.Exception;
import com.safer.safer.common.exception.ExceptionCode;

public class AuthException extends Exception {
    public AuthException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
