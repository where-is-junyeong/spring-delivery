package com.example.springrider.config.filter;

import com.example.springrider.config.Const;
import com.example.springrider.global.exception.AuthException;
import com.example.springrider.global.exception.BaseException;
import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class SessionLoginCheckFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        // 세션 유무를 확인하여 인증 여부 검증
        try {
            if (isLoginRequired(request)) {
                checkSession(request); // 세션 확인

                request.setAttribute(Const.SESSION_USER_ID,
                    request.getSession(false).getAttribute(Const.SESSION_USER_ID));
            }

            filterChain.doFilter(request, response); // 필터 체인 계속 진행
        } catch (BaseException ex) {
            // 예외를 ApiResponse 형태로 반환
            setErrorResponse(response, ex);
        }
    }

    private boolean isLoginRequired(HttpServletRequest request) {
        // 로그인 체크가 필요한 URL을 필터링 (예: 정적 자원 제외, 특정 경로 등)
        String uri = request.getRequestURI();
        return uri.startsWith("/api/") && !uri.equals(Const.LOGIN_URI) && !uri.equals(
            Const.SIGNUP_URI);
    }


    private void checkSession(HttpServletRequest request) {
        if (request.getSession(false) == null
            || request.getSession(false).getAttribute(Const.SESSION_USER_ID) == null) {
            throw new AuthException(ExceptionCode.UNAUTHORIZED);
        }
    }

    private void setErrorResponse(HttpServletResponse response, BaseException ex)
        throws IOException {
        response.setStatus(ex.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Object> apiResponse = ApiResponse.fail(ex);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}