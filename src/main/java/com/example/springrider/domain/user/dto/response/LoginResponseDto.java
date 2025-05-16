package com.example.springrider.domain.user.dto.response;

import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.enums.UserRole;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponseDto {

    private final Long userId;
    private final String email;
    private final String username;
    private final String nickname;
    private final String phone;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final UserRole role;

    public static LoginResponseDto of(User user) {
        return new LoginResponseDto(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getNickname(),
            user.getPhone(),
            user.getCreatedAt(),
            user.getModifiedAt(),
            user.getRole()
        );
    }

}

