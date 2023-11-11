package com.safer.safer.auth.exception;

import com.safer.safer.common.exception.Exception;
import com.safer.safer.common.exception.ExceptionCode;

public class OAuthException extends Exception {

    public OAuthException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public OAuthException(final ExceptionCode exceptionCode, String message) {
        super(exceptionCode, message);
    }
}
