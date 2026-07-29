package com.carumuch.capstone.community.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 자원에 대한 접근 권한이 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다. 관리자에게 문의하세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
