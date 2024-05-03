package com.carumuch.capstone.global.common;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDto<T> {

    private final boolean success;
    private final int status;
    private final String code;
    private final String message;
    private final T response;


    @Builder
    private ResponseDto(boolean success, String code ,String message, int status, T response) {
        this.success = success;
        this.status = status;
        this.code = code;
        this.message = message;
        this.response = response;
    }

    /**
     * 성공 응답
     */
    public static <T> ResponseDto<T> success(HttpStatus status, T data) {
        return ResponseDto.<T>builder()
                .success(true)
                .status(status.value())
                .code(status.name())
                .message("성공적으로 처리되었습니다.")
                .response(data)
                .build();
    }

    /**
     * 실패 응답
     */
    public static <T> ResponseDto<T> fail(HttpStatus status, String message) {
        return ResponseDto.<T>builder()
                .success(false)
                .status(status.value())
                .code(status.name())
                .message(message)
                .response(null)
                .build();
    }

}
