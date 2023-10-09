package com.safer.safer.exception;

import org.springframework.http.HttpStatus;

public class TMapException extends Exception {
    private static final String MESSAGE = "Tmap API 호출에 실패했습니다.";

    public TMapException() {
        super(MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
