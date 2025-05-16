package com.example.springrider.domain.order.dto.request;

import com.example.springrider.domain.order.enums.OrderStatus;
import com.example.springrider.global.validation.EnumValid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateOrderStatusRequestDto {

    @NotNull(message = "Status는 필수 값입니다.")
    @EnumValid(enumClass = OrderStatus.class, message = "올바르지 않은 주문 상태 코드입니다.")
    private final String status;
}
