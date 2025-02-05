package com.carumuch.capstone.global.dto;

import com.carumuch.capstone.global.exception.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Getter
public class CustomErrorResponseDto {
    private final boolean success;
    private final int status;
    private final String code;
    private final String message;

    @Builder
    private CustomErrorResponseDto(boolean success, int status, String code, String message) {
        this.success = success;
        this.status = status;
        this.code = code;
        this.message = message;
    }

    /**
     * exception
     */
    public static ResponseEntity<CustomErrorResponseDto> fail(ErrorCode e){
        log.error(e.getHttpStatus().value() + "에러 발생 -> " + e.getMessage());
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(CustomErrorResponseDto.builder()
                        .success(false)
                        .status(e.getHttpStatus().value())
                        .code(e.name())
                        .message(e.getMessage())
                        .build()
                );
    }
    /**
     * validation
     */
    public static ResponseEntity<CustomErrorResponseDto> valid(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        /* 첫 번째 에러 필드 */
        FieldError fieldError = fieldErrors.get(fieldErrors.size()-1);
        /* 필드값 */
        String fieldName = fieldError.getField();
        /* 입력값 */
        Object rejectedValue = fieldError.getRejectedValue();

        log.error(400 +"에러 발생 -> " + fieldName + " 필드의 입력값[ " + rejectedValue + " ]이 유효하지 않습니다.");
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(CustomErrorResponseDto.builder()
                        .success(false)
                        .status(400)
                        .code(fieldError.getDefaultMessage())
                        .message(fieldName + " 필드의 입력값[ " + rejectedValue + " ]이 유효하지 않습니다.")
                        .build());
    }

    /**
     * validation param
     */
    public static ResponseEntity<CustomErrorResponseDto> validParam(ConstraintViolationException e) {
        String errorMessage = e.getMessage().substring(e.getMessage().indexOf(":") + 1).trim();
        log.error(400 +"에러 발생 -> " + errorMessage);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(CustomErrorResponseDto.builder()
                        .success(false)
                        .status(400)
                        .code(BAD_REQUEST.name())
                        .message(errorMessage)
                        .build());
    }
}
