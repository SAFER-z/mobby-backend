package com.safer.safer.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.safer.safer.common.exception.ExceptionCode.DEFAULT;

@Getter
public class Exception extends RuntimeException {

    private final HttpStatus status;

    public Exception() {
        super(DEFAULT.getMessage());
        this.status = DEFAULT.getStatus();
    }

    public Exception(final ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.status = exceptionCode.getStatus();
    }

    public Exception(final ExceptionCode exceptionCode, final String message) {
        super(exceptionCode.getMessage().concat(message));
        this.status = exceptionCode.getStatus();
    }
}
