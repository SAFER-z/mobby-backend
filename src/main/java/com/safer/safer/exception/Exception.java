package com.safer.safer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "서버 오류입니다.";

    private final HttpStatus status;

    public Exception() {
        super(DEFAULT_MESSAGE);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public Exception(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }
}
