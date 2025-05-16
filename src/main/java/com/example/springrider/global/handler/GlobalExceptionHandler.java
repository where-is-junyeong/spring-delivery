package com.example.springrider.global.handler;

import com.example.springrider.global.exception.BaseException;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.ServerException;
import com.example.springrider.global.response.ApiResponse;
import com.example.springrider.global.response.ErrorResponse;
import com.example.springrider.global.response.ErrorResponse.FieldErrorDetail;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // validation 예외 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Catch Validation Exception: {}", ex.getMessage());

        List<FieldErrorDetail> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> ErrorResponse.FieldErrorDetail.of(
                error.getField(),
                error.getRejectedValue(),
                error.getDefaultMessage()
            ))
            .toList();

        return ApiResponse.fail(ExceptionCode.NOT_VALID_EXCEPTION, fieldErrors);
    }

    // 커스텀 예외 핸들러
    @ExceptionHandler(BaseException.class)
    public ApiResponse<?> handleBaseException(BaseException ex) {
        log.error("Catch Business Exception : {}", ex.getMessage());
        return ApiResponse.fail(ex);
    }

    // 예상치 못한 예외 핸들러
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleGeneralException(Exception e) {
        log.error("Catch General Exception : {}", e.getMessage());
        return ApiResponse.fail(new ServerException(ExceptionCode.INTERNAL_SERVER_ERROR));
    }

}
