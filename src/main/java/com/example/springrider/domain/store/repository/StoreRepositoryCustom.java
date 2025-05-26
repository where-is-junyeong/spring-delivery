package com.example.springrider.domain.store.repository;

import com.example.springrider.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepositoryCustom {
    List<Store> searchByKeywordWithLimit(String keyword,Long offset, Long limit);

    Page<Store> searchByKeywordPaged(@Param("keyword") String keyword, Pageable pageable);
}
