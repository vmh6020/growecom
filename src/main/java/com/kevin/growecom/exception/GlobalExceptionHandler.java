package com.kevin.growecom.exception;

import com.kevin.growecom.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public <T> ResponseEntity<ApiResponse<T>> handleBaseException(BaseException e) {
        return ApiResponse.error(e.getErrorCode().getStatusCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<ApiResponse<T>> handleUnwantedException(Exception e) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
