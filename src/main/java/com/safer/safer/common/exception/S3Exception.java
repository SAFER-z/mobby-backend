package com.safer.safer.common.exception;

public class S3Exception extends Exception {

    public S3Exception(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
