package com.example.springrider.domain.store.repository;

import com.example.springrider.domain.store.dto.response.FindAllStoreResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreQueryRepository {

    Page<FindAllStoreResponseDto> search(String keyword, Pageable pageable);
}
