package com.example.springrider.domain.search.service;

import com.example.springrider.domain.store.dto.response.FindAllStoreResponseDto;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.store.repository.StoreQueryRepositoryImpl;
import com.example.springrider.domain.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public Page<FindAllStoreResponseDto> search(String keyword, Pageable pageable){
        return storeRepository.search(keyword, pageable);
    }

}
