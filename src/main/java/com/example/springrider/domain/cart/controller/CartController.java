package com.example.springrider.domain.cart.controller;

import com.example.springrider.config.Const;
import com.example.springrider.domain.cart.dto.request.CreateCartItemBulkRequestDto;
import com.example.springrider.domain.cart.dto.request.UpdateCartItemRequestDto;
import com.example.springrider.domain.cart.dto.response.CreateCartItemBulkResponseDto;
import com.example.springrider.domain.cart.dto.response.FindCartItemBulkResponseDto;
import com.example.springrider.domain.cart.dto.response.UpdateCartItemResponseDto;
import com.example.springrider.domain.cart.service.CartService;
import com.example.springrider.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Valid
    @PostMapping("/cart-items")
    public ApiResponse<CreateCartItemBulkResponseDto> create(
        @RequestBody CreateCartItemBulkRequestDto requestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID, required = false) Long userId
    ) {
        return ApiResponse.ok(cartService.create(userId, requestDto));
    }

    @GetMapping("/cart-items")
    public ApiResponse<FindCartItemBulkResponseDto> findAll(
        @SessionAttribute(name = Const.SESSION_USER_ID, required = false) Long userId
    ) {
        return ApiResponse.ok(cartService.findAll(userId));
    }

    @PatchMapping("/cart-items/{cartItemId}")
    public ApiResponse<UpdateCartItemResponseDto> update(
        @PathVariable Long cartItemId,
        @Valid @RequestBody UpdateCartItemRequestDto requestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID, required = false) Long userId
    ) {
        return ApiResponse.ok(cartService.update(cartItemId, requestDto, userId));
    }

    @DeleteMapping("/cart-items")
    public ApiResponse<Void> delete(
        @SessionAttribute(name = Const.SESSION_USER_ID, required = false) Long userId) {
        cartService.delete(userId);
        return ApiResponse.ok(null);
    }
}
