package com.example.springrider.domain.order.controller;

import com.example.springrider.config.Const;
import com.example.springrider.domain.order.dto.request.CreateOrderRequestDto;
import com.example.springrider.domain.order.dto.response.OrderResponseDto;
import com.example.springrider.domain.order.service.UserOrderService;
import com.example.springrider.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/customers/orders")
@RequiredArgsConstructor
public class UserOrderController {

    private final UserOrderService userOrderService;

    /**
     * 주문 생성 요청 컨트롤러
     *
     * @param requestDto 주문 정보가 담긴 {@link CreateOrderRequestDto}
     * @return 생성된 주문 정보가 담긴 {@link OrderResponseDto}
     */
    @PostMapping
    public ApiResponse<OrderResponseDto> create(
        @RequestBody CreateOrderRequestDto requestDto,
        @SessionAttribute(name = Const.SESSION_USER_ID) Long userId
    ) {
        return ApiResponse.created(userOrderService.create(requestDto, userId));
    }

    /**
     * 주문 목록 조회 요청 컨트롤러
     *
     * @param userId 유저 식별자
     * @return 고객이 주문한 목록 정보가 담긴 {@link OrderResponseDto}
     */
    @GetMapping
    public ApiResponse<List<OrderResponseDto>> findAll(
        @SessionAttribute(name = Const.SESSION_USER_ID) Long userId) {
        return ApiResponse.ok(userOrderService.findAll(userId));
    }
}
