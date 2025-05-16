package com.example.springrider.domain.user.repository;

import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import com.example.springrider.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email); // 중복여부

    Optional<User> findByEmail(String email); // 이메일로 유저조회

    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.EMAIL_NOT_FOUND));
    }

    default User findByIdOrElseThrow(Long userId) {
        return findById(userId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.USER_NOT_FOUND));
    }

    boolean existsByNicknameAndIdNot(String nickname, Long id);
}
