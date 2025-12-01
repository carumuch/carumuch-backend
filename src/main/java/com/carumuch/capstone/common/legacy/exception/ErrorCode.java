package com.carumuch.capstone.common.legacy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 403 FORBIDDEN : 권한 없음
     */
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 자원에 대한 접근 권한이 없습니다."),

    /**
     * 404 NOT_FOUND : Resource 를 찾을 수 없음
     */
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

    /**
     * 409 : CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재
     */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다."),
    BID_ALREADY_COMPLETED(HttpStatus.CONFLICT,"이미 체결된 견적입니다.."),

    /**
     * 500 INTERNAL_SERVER_ERROR : 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다. 관리자에게 문의하세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
