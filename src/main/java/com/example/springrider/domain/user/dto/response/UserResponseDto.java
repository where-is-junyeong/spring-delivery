package com.example.springrider.domain.user.dto.response;

import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.enums.UserRole;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {

    private final Long userId;
    private final String email;
    private final String name;
    private final String nickname;
    private final String phone;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getNickname(),
            user.getPhone(),
            user.getRole(),
            user.getCreatedAt(),
            user.getModifiedAt()
        );
    }
}
