package com.example.springrider.domain.store.repository;

import com.example.springrider.domain.store.entity.Store;

import java.util.List;

public interface StoreRepositoryCustom {
    List<Store> searchByKeywordWithLimit(String keyword,Long offset, Long limit);
}
