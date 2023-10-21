package com.safer.safer.exception;

public class FileIOException extends Exception{
    public FileIOException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
