package com.safer.safer.routing.exception;

import com.safer.safer.common.exception.Exception;
import com.safer.safer.common.exception.ExceptionCode;

public class AddressException extends Exception {

    public AddressException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public AddressException(final ExceptionCode exceptionCode, String message) {
        super(exceptionCode, message);
    }
}
