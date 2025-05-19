package com.example.springrider.global.handler;

import com.example.springrider.global.exception.BaseException;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.ServerException;
import com.example.springrider.global.response.ApiResponse;
import com.example.springrider.global.response.ErrorResponse.FieldErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@Profile("prod")
@RestControllerAdvice
public class GlobalExceptionHandlerProd {

    // 예상치 못한 예외 핸들러
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleGeneralException(Exception e) {
        log.error("Catch General Exception : {}", e.getMessage());
        return ApiResponse.fail(new ServerException(ExceptionCode.INTERNAL_SERVER_ERROR));
    }

}
