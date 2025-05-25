package com.example.springrider;

import com.example.springrider.config.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.AssertionErrors;

import static org.springframework.test.util.AssertionErrors.assertEquals;


@SpringBootTest
public class JwtUtilTest {

    private static final Logger log = LoggerFactory.getLogger(JwtUtilTest.class);
    @Autowired
    private JwtUtil jwtUtil;


    @Test
    void testGenerateAndParseToken() {
        // given
        Long userId = 1L;
        String email = "test@asciod.exam";

        // when
        String token = jwtUtil.createToken(userId, email,true);
        Claims claims = jwtUtil.getClaims(
                jwtUtil.stripBearer(token));

        // then
        AssertionErrors.assertEquals("subject(UserId) 불일치", userId, Long.valueOf(claims.getSubject()));
        AssertionErrors.assertEquals("email 불일치", email, claims.get("email", String.class));

    }

}
