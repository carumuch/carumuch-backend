package com.carumuch.capstone.common.presentation.advice;

import static org.springframework.http.HttpStatus.*;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.common.presentation.dto.ApiErrorResponse;

import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiErrorResponse<Void>> handleCustomException(CustomException e) {
        if (e.getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            log.error(e.getMessage());
        }
		return ResponseEntity.status(e.getStatus()).body(ApiErrorResponse.of(e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
		return ResponseEntity.status(BAD_REQUEST).body(
                ApiErrorResponse.of(e.getBindingResult().getFieldErrors())
        );
	}

	@ExceptionHandler({
		ObjectOptimisticLockingFailureException.class,
		OptimisticLockingFailureException.class,
		OptimisticLockException.class
	})
	public ResponseEntity<ApiErrorResponse<Void>> handleOptimisticLockException(Exception e) {
		log.warn("동시성 충돌이 발생했습니다. type=optimistic-lock, exception={}, message={}",
			e.getClass().getSimpleName(),
			e.getMessage(),
			e
		);
		return ResponseEntity.status(CONFLICT)
			.body(ApiErrorResponse.of("요청이 동시에 처리되어 작업을 완료하지 못했습니다. 잠시 후 다시 시도해주세요."));
	}
}
