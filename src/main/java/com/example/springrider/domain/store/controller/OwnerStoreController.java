package com.example.springrider.domain.store.controller;

import com.example.springrider.config.Const;
import com.example.springrider.config.security.CustomUserPrincipal;
import com.example.springrider.domain.store.dto.request.StoreRequestDto;
import com.example.springrider.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.springrider.domain.store.dto.response.StoreResponseDto;
import com.example.springrider.domain.store.service.OwnerStoreService;
import com.example.springrider.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OwnerStoreController {

    private final OwnerStoreService ownerStoreService;

    //가게 사장
    @PostMapping("/stores")
    public ApiResponse<StoreResponseDto> create(
        @Valid @RequestBody StoreRequestDto storeRequestDto,
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal
    ) {
        return ApiResponse.created(ownerStoreService.create(storeRequestDto, userPrincipal.getUser()));
    }

    //가게 수정
    @PatchMapping("/stores/{storedId}")
    public ApiResponse<StoreResponseDto> update(
        @Valid @RequestBody UpdateStoreRequestDto requestDto,
        @PathVariable Long storeId,
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(ownerStoreService.update(storeId, requestDto, userPrincipal.getUser()));
    }

    @DeleteMapping("/stores/{storeId}")
    public ApiResponse<StoreResponseDto> delete(
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal
    ) {
        return ApiResponse.ok(ownerStoreService.delete(storeId, userPrincipal.getUser()));
    }
}
