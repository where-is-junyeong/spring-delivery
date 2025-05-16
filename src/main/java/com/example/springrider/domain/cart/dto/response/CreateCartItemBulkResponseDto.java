package com.example.springrider.domain.cart.dto.response;

import com.example.springrider.domain.cart.dto.CreateFailedDto;
import com.example.springrider.domain.cart.dto.CreateSuccessDto;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateCartItemBulkResponseDto {

    private final List<CreateSuccessDto> createSuccessDtos;

    private final List<CreateFailedDto> createFailedDtos;
}
