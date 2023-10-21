package com.safer.safer.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
    DEFAULT("서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    FAIL_TO_PARSE_CSV("Opencsv 관련 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAIL_TO_READ_FILE("파일을 읽어들이는 도중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND("해당 파일을 읽을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    NO_SUCH_FACILITY_TYPE("해당 편의시설 종류를 찾을 수 없습니다: ", HttpStatus.INTERNAL_SERVER_ERROR),
    NO_SUCH_STATION("해당 역을 찾을 수 없습니다 : ", HttpStatus.INTERNAL_SERVER_ERROR),

    FAIL_TO_REQUEST_TMAP_API("TMap API 호출에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;
}
