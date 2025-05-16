package com.example.springrider.domain.menu.controller;

import com.example.springrider.config.Const;
import com.example.springrider.domain.menu.dto.request.MenuRequestDto;
import com.example.springrider.domain.menu.dto.response.MenuResponseDto;
import com.example.springrider.domain.menu.service.MenuService;
import com.example.springrider.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owners/stores")
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 등록 컨트롤러
     *
     * @param storeId 메뉴를 등록할 가게
     * @return {@link ApiResponse} nothing to return
     */
    @PostMapping("/{storeId}/menus")
    public ApiResponse<MenuResponseDto> create(
        @PathVariable Long storeId,
        @Valid @RequestBody MenuRequestDto requestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID) Long userId
    ) {
        return ApiResponse.created(menuService.create(userId, storeId, requestDto));
    }

    /**
     * 메뉴 수정 컨트롤러
     *
     * @param storeId    가게 식별자
     * @param menuId     메뉴 식별자
     * @param requestDto 수정할 메뉴 정보가 담긴 {@link MenuRequestDto}
     * @param userId     세션에 있는 유저 식별자
     * @return 수정된 메뉴 정보가 담긴 {@link MenuResponseDto}
     */
    @PatchMapping("/{storeId}/menus/{menuId}")
    public ApiResponse<MenuResponseDto> update(
        @PathVariable Long storeId, @PathVariable Long menuId,
        @Valid @RequestBody MenuRequestDto requestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID) Long userId
    ) {
        return ApiResponse.ok(menuService.update(storeId, menuId, userId, requestDto));
    }

    /**
     * 메뉴 삭제 컨트롤러
     *
     * @param storeId 가게 식별자
     * @param menuId  메뉴 식별자
     * @param userId  세션에 있는 유저 식별자
     * @return 삭제된 메뉴 정보가 담긴 {@link MenuResponseDto}
     */
    @DeleteMapping("/{storeId}/menus/{menuId}")
    public ApiResponse<MenuResponseDto> delete(
        @PathVariable Long storeId, @PathVariable Long menuId,
        @SessionAttribute(name = Const.SESSION_USER_ID) Long userId
    ) {
        return ApiResponse.ok(menuService.delete(storeId, menuId, userId));
    }

}
