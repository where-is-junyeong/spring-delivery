package com.example.springrider.domain.store.controller;


import com.example.springrider.domain.store.dto.response.FindAllStoreResponseDto;
import com.example.springrider.domain.store.dto.response.FindStoreResponseDto;
import com.example.springrider.domain.store.service.UserStoreService;
import com.example.springrider.global.response.ApiResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserStoreController {

    private final UserStoreService userStoreService;

    //가게 전체
    @GetMapping("/stores")
    public ApiResponse<Map<String, List<FindAllStoreResponseDto>>> finds() {
        List<FindAllStoreResponseDto> stores = userStoreService.finds();
        // HTTP 응답 포맷
        Map<String, List<FindAllStoreResponseDto>> response = new HashMap<>();
        response.put("store", stores);

        return ApiResponse.ok(response);
    }

    //가게 세부 조회
    @GetMapping("/stores/{storeId}")
    public ApiResponse<FindStoreResponseDto> find(@PathVariable Long storeId) {
        FindStoreResponseDto response = userStoreService.find(storeId);
        return ApiResponse.ok(response);
    }
}
