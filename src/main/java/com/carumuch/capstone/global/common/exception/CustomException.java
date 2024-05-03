package com.carumuch.capstone.global.common.exception;

import com.carumuch.capstone.global.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    ErrorCode errorCode;
}