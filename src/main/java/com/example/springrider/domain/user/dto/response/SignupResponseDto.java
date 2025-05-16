package com.example.springrider.domain.user.dto.response;

import com.example.springrider.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignupResponseDto {

    private final String bearerToken;

}
