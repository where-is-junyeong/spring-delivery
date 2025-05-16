package com.example.springrider.domain.user.entity;

import com.example.springrider.domain.common.entity.BaseEntity;
import com.example.springrider.domain.user.dto.request.SignupRequestDto;
import com.example.springrider.domain.user.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 35)
    private String name;

    @Column(nullable = false, length = 35)
    private String nickname;

    @Column(nullable = false, length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private Boolean isWithdraw;

    @Column(nullable = false)
    private Integer storeCount;

    public static User of(
        SignupRequestDto dto, String password, boolean isWithdraw, int storeCount
    ) {
        return new User(
            dto.getEmail(),
            password,
            dto.getName(),
            dto.getNickname(),
            dto.getPhone(),
            dto.getRole(),
            isWithdraw,
            storeCount
        );
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void withdraw() {
        this.isWithdraw = true;
    }

    public void modifyProfile(String nickname, String phone) {
        this.nickname = nickname;
        this.phone = phone;
    }


}
