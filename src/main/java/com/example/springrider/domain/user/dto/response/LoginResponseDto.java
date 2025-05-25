package com.example.springrider.domain.user.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private final String accessToken;
    private final String refreshToken;

}

