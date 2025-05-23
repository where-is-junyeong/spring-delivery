package com.example.springrider.config.jwt;

import com.example.springrider.config.security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 생략 가능
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String bearerJwt = httpRequest.getHeader("Authorization");

        if (bearerJwt != null && bearerJwt.startsWith("Bearer ")) {
            String jwt = jwtUtil.substringToken(bearerJwt);
            try {
                Claims claims = jwtUtil.extractClaims(jwt);

                if (claims != null) {
                    String email = claims.get("email", String.class);

                    // 인증 객체가 없는 경우에만 loadUserByUsername 호출
                    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }

            } catch (SecurityException | MalformedJwtException e) {
                log.error("Invalid JWT signature", e);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 JWT 서명입니다.");
                return;
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token", e);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
                return;
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token", e);
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
                return;
            } catch (Exception e) {
                log.error("JWT 처리 중 알 수 없는 오류", e);
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 생략 가능
    }
}
