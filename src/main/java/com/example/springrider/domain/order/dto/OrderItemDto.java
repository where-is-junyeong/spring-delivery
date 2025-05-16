package com.example.springrider.domain.order.dto;

import com.example.springrider.domain.order.entity.OrderItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderItemDto {

    private final Long menuId;
    private final String menuName;
    private final Integer price;
    private final Integer quantity;

    public static OrderItemDto of(OrderItem orderItem) {
        return new OrderItemDto(
            orderItem.getMenu().getId(),
            orderItem.getMenu().getName(),
            orderItem.getMenu().getPrice(),
            orderItem.getQuantity()
        );
    }

}