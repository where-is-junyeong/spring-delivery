package com.example.springrider.domain.user.service;

import com.example.springrider.config.jwt.JwtTokenProvider;
import com.example.springrider.config.jwt.JwtUtil;
import com.example.springrider.config.security.CustomUserPrincipal;
import com.example.springrider.domain.user.dto.request.DeleteUserRequestDto;
import com.example.springrider.domain.user.dto.request.LoginRequestDto;

import com.example.springrider.domain.user.dto.request.SignupRequestDto;
import com.example.springrider.domain.user.dto.response.LoginResponseDto;
import com.example.springrider.domain.user.dto.response.SignupResponseDto;
import com.example.springrider.domain.user.dto.response.TokenResponse;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.repository.UserRepository;
import com.example.springrider.global.exception.AuthException;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;

    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new AuthException(ExceptionCode.USER_NOT_FOUND));
    }
    /**
     * 회원가입 요청 서비스
     *
     * @param requestDto 유저 정보가 담긴 {@link SignupRequestDto}
     * @return 회원가입된 유저 정보가 담긴 {@link SignupResponseDto}
     */
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new InvalidRequestException(ExceptionCode.EMAIL_ALREADY_USED);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = User.of(requestDto, encodedPassword, false);

        User savedUser = userRepository.save(user);

        CustomUserPrincipal userPrincipal = new CustomUserPrincipal(user);

        //토큰 생성은 JwtTokenProvider 사용
        String accessToken = jwtTokenProvider.generateAccessToken(
                new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, userPrincipal.getAuthorities())
        );

        String refreshToken = jwtTokenProvider.generateRefreshToken(
                new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, userPrincipal.getAuthorities())
        );

        return new SignupResponseDto(accessToken, refreshToken);
    }

    /**
     * 로그인 요청 서비스
     *
     * @param requestDto 로그인 정보가 담긴 {@link LoginRequestDto}
     * @return 로그인 유저 정보가 담긴 {@link LoginResponseDto}
     */
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmailOrElseThrow(requestDto.getEmail());

        if (user.getIsWithdraw()) {
            throw new AuthException(ExceptionCode.ALREADY_DELETED_USER);
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new AuthException(ExceptionCode.PASSWORD_NOT_MATCH);
        }

        CustomUserPrincipal userPrincipal = new CustomUserPrincipal(user); // user는 로그인한 사용자

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    /**
     * 회원 탈퇴 요청 서비스
     *
     * @param requestDto 회원 정보가 담긴 {@link DeleteUserRequestDto}
     * @param userId     유저 식별자
     */
    public void delete(DeleteUserRequestDto requestDto, Long userId) {
        if (userId == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED);
        }
        User user = userRepository.findByIdOrElseThrow(userId);

        // 탈퇴 상태 체크
        if (user.getIsWithdraw()) {
            throw new AuthException(ExceptionCode.ALREADY_DELETED_USER);
        }

        // 이메일 일치 체크
        if (!user.getEmail().equals(requestDto.getEmail())) {
            throw new AuthException(ExceptionCode.ACCESS_DENIED);
        }

        // 비밀번호 일치 체크
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new AuthException(ExceptionCode.PASSWORD_NOT_MATCH);
        }

        user.withdraw(); // 소프트 딜리트
        userRepository.save(user);

    }

    /**
     * 로그아웃 요청 서비스
     *
     *
     */
    public void logout(String bearerToken) {
        jwtTokenProvider.logout(bearerToken);
        // 🔽 Redis에서 Refresh 토큰 삭제 (블랙리스트 방식도 가능)
    }
    public TokenResponse reissue(String refreshToken) {

        String token=jwtTokenProvider.reissueAccessToken(refreshToken);

        return new TokenResponse(token);
    }

    private String buildRefreshKey(String email) {
        return "RT:" + email;
    }

}
