package com.example.springrider.domain.order.dto.response;

import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.order.enums.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateOrderStatusResponseDto {

    private final Long orderId;

    private final OrderStatus orderStatus;

    public static UpdateOrderStatusResponseDto of(Order order) {
        return new UpdateOrderStatusResponseDto(
            order.getId(), order.getStatus()
        );
    }
}
