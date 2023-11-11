package com.safer.safer.common;

import com.safer.safer.auth.exception.AuthException;
import com.safer.safer.auth.exception.OAuthException;
import com.safer.safer.batch.exception.FileIOException;
import com.safer.safer.batch.exception.TMapException;
import com.safer.safer.common.exception.ExceptionResponse;
import com.safer.safer.common.exception.Exception;
import com.safer.safer.common.exception.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({FileIOException.class, TMapException.class})
    public ResponseEntity<ExceptionResponse> dataInputExceptionHandler(final Exception exception) {
        logDebug(exception);
        return ResponseEntity
                .status(exception.getStatus())
                .body(ExceptionResponse.from(exception));
    }

    @ExceptionHandler({NoSuchElementException.class, OAuthException.class, AuthException.class})
    public ResponseEntity<ExceptionResponse> noSuchElementExceptionHandler(final Exception exception) {
        logWarn(exception);
        return ResponseEntity
                .status(exception.getStatus())
                .body(ExceptionResponse.from(exception));
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
