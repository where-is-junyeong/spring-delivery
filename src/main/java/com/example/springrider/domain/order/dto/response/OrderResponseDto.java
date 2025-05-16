package com.example.springrider.domain.order.dto.response;

import com.example.springrider.domain.order.dto.OrderItemDto;
import com.example.springrider.domain.order.entity.Order;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderResponseDto {

    private final Long orderId;
    private final String storeName;
    private final String deliveryAddress;
    private final Integer totalPrice;
    private final String status;
    private final LocalDateTime createdAt;
    private final List<OrderItemDto> orderItems;

    // storeName을 제외하기 위한 private 생성자
    private OrderResponseDto(
        Long orderId,
        String deliveryAddress,
        Integer totalPrice,
        String status,
        LocalDateTime createdAt,
        List<OrderItemDto> orderItems
    ) {
        this(orderId, null, deliveryAddress, totalPrice, status, createdAt, orderItems);
    }


    public static OrderResponseDto of(Order order) {
        return new OrderResponseDto(
            order.getId(),
            order.getStore().getName(),
            order.getDeliveryAddress(),
            order.getTotalPrice(),
            order.getStatus().name(),
            order.getCreatedAt(),
            order.getOrderItems().stream()
                .map(OrderItemDto::of)
                .toList()
        );
    }

    public static OrderResponseDto ofOwner(Order order) {
        return new OrderResponseDto(
            order.getId(),
            order.getDeliveryAddress(),
            order.getTotalPrice(),
            order.getStatus().name(),
            order.getCreatedAt(),
            order.getOrderItems().stream()
                .map(OrderItemDto::of)
                .toList()
        );
    }

}