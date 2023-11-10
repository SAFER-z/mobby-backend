package com.safer.safer.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
    DEFAULT("서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    FAIL_TO_PARSE_CSV("Opencsv 관련 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAIL_TO_READ_FILE("파일을 읽어들이는 도중 오류가 발생했습니다: ", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND("해당 파일을 읽을 수 없습니다: ", HttpStatus.INTERNAL_SERVER_ERROR),

    NO_SUCH_FACILITY_TYPE("편의시설 종류를 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    NO_SUCH_STATION("역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NO_SUCH_FACILITY("편의시설을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    FAIL_TO_REQUEST_TMAP_API("TMap API 호출에 실패했습니다: ", HttpStatus.INTERNAL_SERVER_ERROR),

    FAIL_TO_REQUEST_TOKEN("OAuth 서버에 인증을 요청하는 과정에서 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAIL_TO_REQUEST_USER_INFO("OAuth 서버에 사용자 정보를 요청하는 과정에서 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNSUPPORTED_OAUTH_TYPE("지원하지 않는 소셜 로그인 종류입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}
