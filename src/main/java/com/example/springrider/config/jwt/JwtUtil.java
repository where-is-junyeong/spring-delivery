package com.example.springrider.config.jwt;

import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.ServerException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKeyEncoded;

    @Value("${jwt.token.access-exp}")
    private long accessTokenExpiry;

    @Value("${jwt.token.refresh-exp}")
    private long refreshTokenExpiry;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyEncoded));
    }

    public String createToken(Long userId, String email, boolean isAccessToken) {
        long expiry = isAccessToken ? accessTokenExpiry : refreshTokenExpiry;
        String tokenType = isAccessToken ? "ACCESS" : "REFRESH";

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("type", tokenType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new ServerException(ExceptionCode.EXPIRED_JWT);
        } catch (Exception e) {
            throw new ServerException(ExceptionCode.INVALID_JWT);
        }
    }

    public long getRefreshExpiry() {
        return refreshTokenExpiry;
    }
}
