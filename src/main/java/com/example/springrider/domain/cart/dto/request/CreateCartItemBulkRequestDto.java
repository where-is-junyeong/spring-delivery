package com.example.springrider.domain.cart.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateCartItemBulkRequestDto {

    @NotNull(message = "가게Id는 필수 값입니다.")
    private final Long storeId;

    @NotEmpty(message = "1개 이상의 메뉴를 요청해야 합니다.")
    private final List<CreateCartItemRequestDto> cartItems;
}
