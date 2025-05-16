package com.example.springrider.domain.order.controller;

import com.example.springrider.config.Const;
import com.example.springrider.domain.order.dto.request.CancelOrderRequestDto;
import com.example.springrider.domain.order.dto.request.UpdateOrderStatusRequestDto;
import com.example.springrider.domain.order.dto.response.CancelOrderResponseDto;
import com.example.springrider.domain.order.dto.response.OwnerOrderResponseDto;
import com.example.springrider.domain.order.dto.response.UpdateOrderStatusResponseDto;
import com.example.springrider.domain.order.service.OwnerOrderService;
import com.example.springrider.global.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/owners/stores")
@RequiredArgsConstructor
public class OwnerOrderController {

    private final OwnerOrderService ownerOrderService;

    @PatchMapping("/{storeId}/orders/{orderId}")
    public ApiResponse<UpdateOrderStatusResponseDto> update(
        @PathVariable Long storeId, @PathVariable Long orderId,
        @Valid @RequestBody UpdateOrderStatusRequestDto requestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID) Long userId
    ) {
        return ApiResponse.ok(ownerOrderService.update(orderId, storeId, userId, requestDto));
    }

    @GetMapping("/{storeId}/orders")
    public ApiResponse<List<OwnerOrderResponseDto>> findAll(
        @PathVariable Long storeId,
        @SessionAttribute(name = Const.SESSION_USER_ID) Long userId
    ) {
        return ApiResponse.ok(ownerOrderService.findAll(storeId, userId));
    }

    @DeleteMapping("/{storeId}/orders/{orderId}")
    public ApiResponse<CancelOrderResponseDto> delete(
        @PathVariable Long storeId, @PathVariable Long orderId,
        @Valid @RequestBody CancelOrderRequestDto requestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID) Long userId
    ) {
        return ApiResponse.ok(ownerOrderService.delete(storeId, orderId, userId, requestDto));
    }
}
