package com.example.springrider.domain.user.dto.response;

import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.enums.UserRole;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponseDto {

    private final String bearerToken;

}

