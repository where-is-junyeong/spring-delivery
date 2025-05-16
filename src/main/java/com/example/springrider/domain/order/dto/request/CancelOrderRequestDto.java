package com.example.springrider.domain.order.dto.request;

import com.example.springrider.domain.order.enums.OrderCancelReason;
import com.example.springrider.global.validation.EnumValid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CancelOrderRequestDto {

    @NotNull
    @EnumValid(enumClass = OrderCancelReason.class, message = "올바르지 않은 주문 취소 코드입니다.")
    private final String cancelReason;

    @NotNull
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).*$",
        message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private final String password;

    @Size(max = 20, message = "메세지 최대 글자수는 20자 입니다.")
    private final String cancelMessage;
}
