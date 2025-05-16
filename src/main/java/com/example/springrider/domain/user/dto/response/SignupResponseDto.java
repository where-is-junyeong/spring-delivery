package com.example.springrider.domain.user.dto.response;

import com.example.springrider.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignupResponseDto {

    private final Long userId;
    private final String email;
    private final String username;
    private final String nickname;
    private final String phone;
    private final String role;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static SignupResponseDto of(User user) {
        return new SignupResponseDto(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getNickname(),
            user.getPhone(),
            user.getRole().name(),
            user.getCreatedAt(),
            user.getModifiedAt()
        );
    }
}
