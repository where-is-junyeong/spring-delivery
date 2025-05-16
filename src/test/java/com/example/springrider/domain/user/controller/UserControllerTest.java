package com.example.springrider.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springrider.config.interceptor.StoreOwnerInterceptor;
import com.example.springrider.domain.user.dto.request.LoginRequestDto;
import com.example.springrider.domain.user.dto.request.SignupRequestDto;
import com.example.springrider.domain.user.dto.response.LoginResponseDto;
import com.example.springrider.domain.user.dto.response.SignupResponseDto;
import com.example.springrider.domain.user.enums.UserRole;
import com.example.springrider.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private StoreOwnerInterceptor storeOwnerInterceptor;

    @MockBean
    private UserService userService;


    @Test
    @DisplayName("회원가입 성공")
    void signup_success() throws Exception {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto(
            "test@email.com", "Pass123!", "홍길동", "hong", "010-1234-5678", UserRole.USER
        );

        SignupResponseDto responseDto = new SignupResponseDto(
            1L, "test@email.com", "홍길동", "hong", "010-1234-5678", "USER",
            LocalDateTime.now(), LocalDateTime.now()
        );

        Mockito.when(userService.signup(any())).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.username").value("홍길동"))
            .andExpect(jsonPath("$.data.nickname").value("hong"));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        // Given
        LoginRequestDto requestDto = new LoginRequestDto("test@email.com", "Pass123!");

        LoginResponseDto responseDto = new LoginResponseDto(
            1L,
            "test@email.com",
            "홍길동",
            "hong",
            "010-1234-5678",
            LocalDateTime.now(),
            LocalDateTime.now(),
            UserRole.USER
        );

        Mockito.when(userService.login(any(LoginRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.nickname").value("hong"));
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1L);

        // When & Then
        mockMvc.perform(delete("/api/users/logout")
                .session(session))
            .andExpect(status().isOk());
    }
}
