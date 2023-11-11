package com.safer.safer.batch.exception;

import com.safer.safer.common.exception.Exception;
import com.safer.safer.common.exception.ExceptionCode;

public class FileIOException extends Exception {
    public FileIOException(final ExceptionCode exceptionCode, final String filePath) {
        super(exceptionCode, filePath);
    }
}
