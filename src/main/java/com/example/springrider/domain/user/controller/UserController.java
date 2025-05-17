package com.example.springrider.domain.user.controller;

import com.example.springrider.domain.user.dto.request.LoginRequestDto;
import com.example.springrider.domain.user.dto.request.SignupRequestDto;
import com.example.springrider.domain.user.dto.response.LoginResponseDto;
import com.example.springrider.domain.user.dto.response.SignupResponseDto;
import com.example.springrider.domain.user.service.UserService;
import com.example.springrider.global.exception.AuthException;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.response.ApiResponse;
import com.example.springrider.global.response.ResponseMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/signup")
    public ApiResponse<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return ApiResponse.created(userService.signup(requestDto));
    }
    //로그인
    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(
        @Valid @RequestBody LoginRequestDto requestDto, HttpSession session) {

        return ApiResponse.ok(userService.login(requestDto));
    }
    //로그아웃
    @DeleteMapping("/logout")
    public ApiResponse<String> logout(
        @SessionAttribute(name = "userId", required = false) Long userId, HttpSession session) {
        validateSession(userId);
        session.invalidate();
        return ApiResponse.ok(ResponseMessage.LOGOUT_SUCCESS.getMessage());
    }

    private void validateSession(Long userId) {
        if (userId == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED);
        }
    }
}