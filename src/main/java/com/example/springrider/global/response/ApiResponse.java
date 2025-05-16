package com.example.springrider.global.response;

import com.example.springrider.global.exception.BaseException;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.response.ErrorResponse.FieldErrorDetail;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int statusCode;
    private final T data;
    private final ErrorResponse error;

    private ApiResponse(int statusCode, T data, ErrorResponse error) {
        this.statusCode = statusCode;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> ok(final T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), data, null);
    }

    public static <T> ApiResponse<T> created(final T data) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), data, null);
    }

    public static ApiResponse<Object> fail(final BaseException ex) {
        return new ApiResponse<>(ex.getStatus().value(), null,
            ErrorResponse.of(ex.getExceptionCode()));
    }

    public static ApiResponse<Object> fail(ExceptionCode code, List<FieldErrorDetail> fieldErrors) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), null,
            ErrorResponse.of(code, fieldErrors));
    }

}
