package com.example.springrider.domain.store.controller;

import com.example.springrider.config.Const;
import com.example.springrider.domain.store.dto.request.StoreRequestDto;
import com.example.springrider.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.springrider.domain.store.dto.response.StoreResponseDto;
import com.example.springrider.domain.store.service.OwnerStoreService;
import com.example.springrider.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OwnerStoreController {

    private final OwnerStoreService ownerStoreService;

    /**
     * 가게 생성 (사장만)
     *
     * @param storeRequestDto 가게 정보 요청 dto
     * @param userId          로그인 상태인 유저의 세션 정보
     * @return 가게 정보 + 유저 이름
     */
    @PostMapping("/owners/stores")
    public ApiResponse<StoreResponseDto> create(
        @Valid @RequestBody StoreRequestDto storeRequestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID) Long userId
    ) {
        return ApiResponse.created(ownerStoreService.create(storeRequestDto, userId));
    }

    /**
     * 가게 수정 (사장만)
     *
     * @param storeId    수정할 가게의 고유 식별자
     * @param requestDto 수정할 정보가 담긴 dto
     * @param userId     로그인 상태인 유저의 세션 정보
     * @return 수정된 가게 정보 + 유저 정보
     */
    @PutMapping("/owners/stores/{storeId}")
    public ApiResponse<StoreResponseDto> update(
        @Valid @RequestBody UpdateStoreRequestDto requestDto,
        @PathVariable Long storeId,
        @SessionAttribute(name = Const.SESSION_USER_ID, required = false) Long userId
    ) {
        return ApiResponse.ok(ownerStoreService.update(storeId, requestDto, userId));
    }
}
