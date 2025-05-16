package com.example.springrider.domain.user.controller;

import com.example.springrider.config.Const;
import com.example.springrider.domain.user.dto.request.DeleteUserRequestDto;
import com.example.springrider.domain.user.dto.request.LoginRequestDto;
import com.example.springrider.domain.user.dto.request.PasswordModifyRequestDto;
import com.example.springrider.domain.user.dto.request.ProfileModifyRequestDto;
import com.example.springrider.domain.user.dto.request.SignupRequestDto;
import com.example.springrider.domain.user.dto.response.LoginResponseDto;
import com.example.springrider.domain.user.dto.response.SignupResponseDto;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.service.UserService;
import com.example.springrider.global.exception.AuthException;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.response.ApiResponse;
import com.example.springrider.global.response.ResponseMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 요청 컨트롤러
     *
     * @param requestDto 회원가입 정보가 담긴 {@link SignupRequestDto}
     * @return 생성된 회원 정보가 담긴 {@link SignupResponseDto}
     */
    @PostMapping("/signup")
    public ApiResponse<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return ApiResponse.created(userService.signup(requestDto));
    }

    /**
     * 로그인 요청 컨트롤러
     *
     * @param requestDto 회원가입 정보가 담긴 {@link LoginRequestDto}
     * @param session    세션 정보
     * @return 로그인 정보가 담긴 {@link LoginResponseDto}
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(
        @Valid @RequestBody LoginRequestDto requestDto, HttpSession session) {
        LoginResponseDto responseDto = userService.login(requestDto);
        session.setAttribute("userId", responseDto.getUserId());
        return ApiResponse.ok(responseDto);
    }

    /**
     * 회원 탈퇴 요청 컨트롤러
     *
     * @param requestDto 회원 탈퇴 요청 정보가 담긴 {@link DeleteUserRequestDto}
     * @param userId     세션에 담긴 유저 식별자
     * @param session    세션 정보
     * @return 200 ok
     */
    @DeleteMapping("/withdraw") // 회원탈퇴
    public ApiResponse<String> delete(
        @Valid @RequestBody DeleteUserRequestDto requestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID, required = false) Long userId,
        HttpSession session
    ) {
        validateSession(userId);
        userService.delete(requestDto, userId);
        session.invalidate();
        return ApiResponse.ok(ResponseMessage.USER_DELETED.getMessage());
    }

    /**
     * 비밀번호 수정 요청 컨트롤러
     *
     * @param requestDto 수정할 비밀번호 정보가 담긴 {@link PasswordModifyRequestDto}
     * @param userId     유저 식별자
     * @return 200 ok
     */
    @PutMapping("/password") // 비밀번호 수정
    public ApiResponse<String> modifyPassword(
        @Valid @RequestBody PasswordModifyRequestDto requestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID, required = false) Long userId,
        HttpSession session
    ) {
        validateSession(userId);
        userService.modifyPassword(requestDto, userId);
        session.invalidate();
        return ApiResponse.ok(ResponseMessage.PASSWORD_MODIFY_SUCCESS.getMessage());
    }

    /**
     * 로그아웃 요청 컨트롤러
     *
     * @param session 세션 정보
     * @return 200 ok
     */
    @DeleteMapping("/logout") // 로그아웃
    public ApiResponse<String> logout(
        @SessionAttribute(name = "userId", required = false) Long userId, HttpSession session) {
        validateSession(userId);
        session.invalidate();
        return ApiResponse.ok(ResponseMessage.LOGOUT_SUCCESS.getMessage());
    }

    @PatchMapping("/profile")
    public ApiResponse<String> updateProfile(
        @Valid @RequestBody ProfileModifyRequestDto requestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID, required = false) Long userId
    ) {
        // 로그인 여부 확인
        validateSession(userId);

        userService.updateProfile(requestDto, userId);

        User updatedUser = userService.findById(userId);
        return ApiResponse.ok(ResponseMessage.PROFILE_UPDATE_SUCCESS.getMessage());

    }

    private void validateSession(Long userId) {
        if (userId == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED);
        }
    }
}