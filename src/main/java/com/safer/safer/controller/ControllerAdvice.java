package com.safer.safer.controller;

import com.safer.safer.dto.ErrorResponse;
import com.safer.safer.exception.*;
import com.safer.safer.exception.Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({FileIOException.class, TMapException.class})
    public ResponseEntity<ErrorResponse> dataInputExceptionHandler(final Exception exception) {
        logDebug(exception);
        return ResponseEntity
                .status(exception.getStatus())
                .body(ErrorResponse.from(exception));
    }

    @ExceptionHandler({NoSuchElementException.class, OAuthException.class})
    public ResponseEntity<ErrorResponse> noSuchElementExceptionHandler(final Exception exception) {
        logWarn(exception);
        return ResponseEntity
                .status(exception.getStatus())
                .body(ErrorResponse.from(exception));
    }

    private void logDebug(Exception exception) {
        log.debug(exception.getMessage(), exception);
    }

    private void logInfo(Exception exception) {
        log.info(exception.getMessage(), exception);
    }

    private void logWarn(Exception exception) {
        log.warn(exception.getMessage(), exception);
    }

    private void logError(Exception exception) {
        log.error(exception.getMessage(), exception);
    }
}
