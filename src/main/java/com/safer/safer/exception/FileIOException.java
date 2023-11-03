package com.safer.safer.exception;

public class FileIOException extends Exception{
    public FileIOException(final ExceptionCode exceptionCode, final String filePath) {
        super(exceptionCode, filePath);
    }
}
