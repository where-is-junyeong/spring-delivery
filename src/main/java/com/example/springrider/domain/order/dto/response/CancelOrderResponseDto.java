package com.example.springrider.domain.order.dto.response;

import com.example.springrider.domain.order.entity.Order;
import com.example.springrider.domain.order.enums.OrderCancelReason;
import com.example.springrider.domain.order.enums.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CancelOrderResponseDto {

    private final Long orderId;

    private final OrderStatus status;

    private final OrderCancelReason cancelReason;

    private final String cancelMessage;

    public static CancelOrderResponseDto of(Order order) {
        return new CancelOrderResponseDto(
            order.getId(), order.getStatus(), order.getCancelReason(),
            order.getCancelMessage()
        );
    }
}
