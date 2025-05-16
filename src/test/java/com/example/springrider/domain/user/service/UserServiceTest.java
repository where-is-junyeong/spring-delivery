package com.example.springrider.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.example.springrider.domain.user.dto.request.DeleteUserRequestDto;
import com.example.springrider.domain.user.dto.request.LoginRequestDto;
import com.example.springrider.domain.user.dto.request.PasswordModifyRequestDto;
import com.example.springrider.domain.user.dto.request.ProfileModifyRequestDto;
import com.example.springrider.domain.user.dto.request.SignupRequestDto;
import com.example.springrider.domain.user.dto.response.LoginResponseDto;
import com.example.springrider.domain.user.dto.response.SignupResponseDto;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.enums.UserRole;
import com.example.springrider.domain.user.repository.UserRepository;
import com.example.springrider.global.security.DefaultPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DefaultPasswordEncoder defaultPasswordEncoder;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    @Test
    void signup_성공() {
        // Given
        SignupRequestDto request = new SignupRequestDto(
            "user@test.com", "pass123!", "홍길동", "hong", "01012345678", UserRole.USER
        );

        when(userRepository.existsByEmail("user@test.com")).thenReturn(false);
        when(defaultPasswordEncoder.encode("pass123!")).thenReturn("encodedPwd");

        User savedUser = User.of(request, "encodedPwd", false, 0);
        ReflectionTestUtils.setField(savedUser, "id", 1L);
        when(userRepository.save(any())).thenReturn(savedUser);

        // When
        SignupResponseDto response = userService.signup(request);

        // Then
        assertThat(response.getUsername()).isEqualTo("홍길동");
        assertThat(response.getNickname()).isEqualTo("hong");
    }

    @Test
    void login_성공() {
        // Given
        LoginRequestDto request = new LoginRequestDto("user@test.com", "pass123!");
        User user = new User("user@test.com", "encodedPwd", "홍길동", "hong", "010", UserRole.USER,
            false, 0);
        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.findByEmailOrElseThrow("user@test.com")).thenReturn(user);
        when(defaultPasswordEncoder.matches("pass123!", "encodedPwd")).thenReturn(true);

        // When
        LoginResponseDto response = userService.login(request);

        // Then
        assertThat(response.getNickname()).isEqualTo("hong");
        assertThat(session.getAttribute("userId")).isEqualTo(1L);
    }

    @Test
    void delete_성공() {
        // Given
        DeleteUserRequestDto request = new DeleteUserRequestDto("user@test.com", "010-9999-9999");
        User user = new User("user@test.com", "encodedPwd", "홍길동", "hong", "010", UserRole.USER,
            false, 0);
        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.findByIdOrElseThrow(1L)).thenReturn(user);
        when(defaultPasswordEncoder.matches("010-9999-9999", "encodedPwd")).thenReturn(true);

        session.setAttribute("userId", 1L);

        // When
        userService.delete(request, 1L);

        // Then
        assertThat(user.getIsWithdraw()).isTrue();
        assertThat(session.isInvalid()).isTrue();
    }

    @Test
    void modifyPassword_성공() {
        // Given
        PasswordModifyRequestDto request = new PasswordModifyRequestDto("oldPwd!", "newPwd!");
        User user = new User("user@test.com", "encodedOld", "홍길동", "hong", "010", UserRole.USER,
            false, 0);
        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.findByIdOrElseThrow(1L)).thenReturn(user);
        when(defaultPasswordEncoder.matches("oldPwd!", "encodedOld")).thenReturn(true);
        when(defaultPasswordEncoder.encode("newPwd!")).thenReturn("encodedNew");

        // When
        userService.modifyPassword(request, 1L);

        // Then
        assertThat(user.getPassword()).isEqualTo("encodedNew");
        assertThat(session.isInvalid()).isTrue();
    }

    @Test
    void logout_성공() {
        // Given
        session.setAttribute("userId", 1L);

        // When
        userService.logout(session);

        // Then
        assertThat(session.isInvalid()).isTrue();
    }

    @Test
    void updateProfile_성공() {
        // Given
        ProfileModifyRequestDto request = new ProfileModifyRequestDto("newNick", "010-9999-9999",
            "pass 123!");
        User user = new User(
            "user@test.com",
            "encodedPwd",
            "홍길동",
            "hong",
            "010",
            UserRole.USER,
            false,
            0
        );
        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.findByIdOrElseThrow(1L)).thenReturn(user);
        when(defaultPasswordEncoder.matches("pass123!", "encodedPwd")).thenReturn(true);
        when(userRepository.existsByNicknameAndIdNot("newNick", 1L)).thenReturn(false);

        // When
        userService.updateProfile(request, 1L);

        // Then
        assertThat(user.getNickname()).isEqualTo("newNick");
        assertThat(user.getPhone()).isEqualTo("010-9999-9999");
    }

}
