package com.example.springrider.domain.store.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.example.springrider.domain.store.dto.request.StoreRequestDto;
import com.example.springrider.domain.store.dto.response.StoreResponseDto;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.store.service.OwnerStoreService;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OwnerStoreController.class)
class StoreControllerTest {

    @MockitoBean
    OwnerStoreService ownerStoreService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("가게 등록에 성공하면 JSON 응답 반환")
    void createStore_shouldReturnStoreInfo() throws Exception {

        // given: 가게 정보를 정확히 기입했다고 가정
        String name = "김태정의 레전드 맛집";
        String address = "서울특별시";
        String category = "한식";
        LocalTime openTime = LocalTime.of(9, 0, 0);
        LocalTime closeTime = LocalTime.of(20, 0, 0);
        Integer minOrderPrice = 2000;
        StoreStatus storeStatus = StoreStatus.ACTIVE;

        StoreRequestDto storeRequestDto = new StoreRequestDto(
            name, address, category, openTime, closeTime, minOrderPrice, storeStatus, 1L
        );

        // 가게 생성 요청 시 응답 dto도 생성
        StoreResponseDto storeResponseDto = new StoreResponseDto(
            1L, name, address, category, openTime, closeTime, minOrderPrice, storeStatus, "김태정"
        );

        given(ownerStoreService.create(any(StoreRequestDto.class), anyLong()))
            .willReturn(storeResponseDto);

        // when: 가게를 생성하는 컨트롤러 호출 <- 여기부터 막힘...
        // then: 가게 정보 확인
//        mockMvc.perform(post("/owners/stores")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(storeRequestDto))
//                .sessionAttr("userId", 1L)) // 세션에 userId 추가
//            .andExpect(status().isCreated())
//            .andExpect(jsonPath("$.data.name").value(name))
//            .andExpect(jsonPath("$.data.address").value(address))
//            .andExpect(jsonPath("$.data.category").value(category))
//            .andExpect(jsonPath("$.data.openTime").value(openTime.toString()))
//            .andExpect(jsonPath("$.data.closeTime").value(closeTime.toString()))
//            .andExpect(jsonPath("$.data.minOrderPrice").value(minOrderPrice))
//            .andExpect(jsonPath("$.data.storeStatus").value(storeStatus.toString()))
//            .andExpect(jsonPath("$.data.ownerName").value("김태정"));
    }
}