package com.example.springrider.domain.search.service;

import com.example.springrider.domain.search.dto.response.TrendingKeywordResponseDto;
import com.example.springrider.domain.search.entity.Search;
import com.example.springrider.domain.search.repository.SearchRepository;
import com.example.springrider.domain.store.dto.response.FindAllStoreResponseDto;
import com.example.springrider.domain.store.repository.StoreQueryRepository;
import com.example.springrider.domain.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreRepository storeRepository;
    private final SearchRepository searchRepository;

    public Page<FindAllStoreResponseDto> search(String keyword, Pageable pageable) {
        // search 테이블은 검색 이력 테이블이 아닌 누적 카운트 테이블로 구현
        Search search = searchRepository.findByKeyword(keyword)
            // keyword 가 존재하면 count += 1 한 객체 반환
            .map(s -> {
                s.increaseCount(); // search.count++
                return s;
            })
            // keyword 가 존재하지 않으면 count = 1인 새 객체 생성
            .orElseGet(() -> Search.of(keyword, 1L));

        searchRepository.save(search);

        return storeRepository.search(keyword, pageable);
    }

    public List<TrendingKeywordResponseDto> trending(Long rank){
        return searchRepository.trendingKeyword(rank);
    }

}
