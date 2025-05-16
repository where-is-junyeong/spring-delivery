package com.example.springrider.domain.order.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateOrderRequestDto {

    private String address;

}
