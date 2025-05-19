package com.example.springrider.domain.search.service;


import com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto;
import com.example.springrider.domain.search.entity.SearchLog;
import com.example.springrider.domain.search.entity.Trending;
import com.example.springrider.domain.search.repository.SearchLogRepository;
import com.example.springrider.domain.search.repository.TrendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;
    private final TrendingRepository trendingRepository;
    //검색결과 도출
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(String keyword){

        SearchLog searchLog=new SearchLog(keyword);

        searchLogRepository.save(searchLog);

    }
    // 시간 위주(배달에는 적절)
    public Page<SearchTrendingResponseDto> findV1(Pageable pageable){

        return trendingRepository.findTrending(pageable);
    }

    // 실시간 인기 검색
    @Transactional(readOnly = true)
    @Cacheable(value = "searchCacheLog", key = "'trending:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<SearchTrendingResponseDto> findV2(Pageable pageable){
        LocalDateTime localDateTime=LocalDateTime.now().minusHours(1);
        System.out.println("🔥 DB HIT!");
        return searchLogRepository.findTrending(localDateTime,pageable);
    }

    @Transactional
    public void updateTrending() {
        LocalDateTime anHourAgo = LocalDateTime.now().minusHours(1);
        Page<SearchTrendingResponseDto> trendingKeywords =
                searchLogRepository.findTrending(anHourAgo, PageRequest.of(0, 10));

        trendingRepository.deleteAllInBatch();

        List<Trending> trendingList = trendingKeywords.stream()
                .map(dto -> new Trending(dto.getKeyword(), dto.getCount()))
                .toList();

        trendingRepository.saveAll(trendingList);
    }

    @Transactional
    public void deleteOldSearchLogs() {
        LocalDateTime aWeekAgo = LocalDateTime.now().minusWeeks(1);
        searchLogRepository.deleteSearchLogByCreatedAtBefore(aWeekAgo);
    }


}
