package com.safer.safer.batch.exception;

import com.safer.safer.common.exception.Exception;
import com.safer.safer.common.exception.ExceptionCode;

public class TMapException extends Exception {

    public TMapException(final ExceptionCode exceptionCode, String message) {
        super(exceptionCode, message);
    }
}
