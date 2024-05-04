package com.carumuch.capstone.global.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    /**
     * exception
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CustomErrorResponseDto> handleCustomException(CustomException e) {
        return CustomErrorResponseDto.fail(e.getErrorCode());
    }

    /**
     * validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<CustomErrorResponseDto> handleValidationException(MethodArgumentNotValidException e) {
        return CustomErrorResponseDto.valid(e);
    }

    /**
     * validation param
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        return CustomErrorResponseDto.validParam(e);
    }
}
