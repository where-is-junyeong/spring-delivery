package com.example.springrider.config.jwt;

import com.example.springrider.config.security.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserDetailsService userDetailsService;

    private static final String TOKEN_PREFIX = "Bearer ";

    public String generateAccessToken(Authentication auth) {
        CustomUserPrincipal user = (CustomUserPrincipal) auth.getPrincipal();
        return jwtUtil.createToken(user.getUserId(), user.getUsername(), true);
    }

    public String generateRefreshToken(Authentication auth) {
        CustomUserPrincipal user = (CustomUserPrincipal) auth.getPrincipal();
        String refreshToken = jwtUtil.createToken(user.getUserId(), user.getUsername(), false);

        String redisKey = buildRefreshKey(user.getUsername());
        redisTemplate.opsForValue().set(
                redisKey,
                refreshToken,
                jwtUtil.getRefreshExpiry(),
                TimeUnit.MILLISECONDS
        );
        return refreshToken;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtUtil.getClaims(token);
        String email = claims.get("email", String.class);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith(TOKEN_PREFIX)) {
            return bearer.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validate(String token) {
        if (isBlacklisted(token)) {
            throw new RuntimeException("Access token is blacklisted");
        }
        return jwtUtil.isValid(token);
    }

    public boolean logout(String bearerToken) {
        String token = stripBearer(bearerToken);
        Claims claims = jwtUtil.getClaims(token);
        long expiration = claims.getExpiration().getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue().set("BL:" + token, "logout", expiration, TimeUnit.MILLISECONDS);
        return true;
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("BL:" + token));
    }

    /**
     * Refresh Token 재발급 로직
     * @param refreshToken 클라이언트가 보낸 Refresh Token (Bearer 제외)
     * @return 재발급된 Access Token
     */
    public String reissueAccessToken(String refreshToken) {
        // 1. Refresh Token 유효성 검사
        if (!jwtUtil.isValid(refreshToken)) {
            throw new RuntimeException("Invalid Refresh Token");
        }

        // 2. Redis에서 저장된 Refresh Token과 비교
        Claims claims = jwtUtil.getClaims(refreshToken);
        String email = claims.get("email", String.class);
        String savedRefreshToken = redisTemplate.opsForValue().get(buildRefreshKey(email));

        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            throw new RuntimeException("Refresh Token mismatch or expired");
        }

        // 3. UserDetails 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // 4. 새 Access Token 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        return generateAccessToken(authentication);
    }

    private String buildRefreshKey(String email) {
        return "RT:" + email;
    }

    private String stripBearer(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        throw new RuntimeException("Invalid Authorization header");
    }
}
