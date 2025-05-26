package com.example.springrider.domain.search.service;

import com.example.springrider.config.security.CustomUserPrincipal;
import com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto;
import com.example.springrider.domain.search.repository.SearchLogBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SearchLogBatchService {

    private final SearchLogBatchRepository searchLogRepository;

    /**
     * 즉시 DB에 검색 로그 저장 (트랜잭션 별도 분리)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(String keyword) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()){
            LocalDateTime now = LocalDateTime.now();

            int time = now.getYear()*1000000
                    + now.getMonthValue()*10000
                    + now.getDayOfMonth()*100
                    + now.getHour();

            Long userId = ((CustomUserPrincipal) auth.getPrincipal()).getUser().getId();

            searchLogRepository.insertIgnore(time, keyword, userId);
        }
    }

//    /**
//     * 최근 1시간 기준 인기 검색어 (DB 기반)
//     */
//    public Page<SearchTrendingResponseDto> findV1Batch(Pageable pageable) {
//
//        return trendingRepository.findTrending(pageable);
//    }
//
//    /**
//     * 1시간 전까지 검색 로그 기반으로 트렌딩 데이터 갱신
//     */
//    @Transactional
//    public void updateTrending() {
//        LocalDateTime anHourAgo = LocalDateTime.now().minusHours(1);
//        Page<SearchTrendingResponseDto> trendingKeywords =
//                searchLogRepository.findTrending(anHourAgo, PageRequest.of(0, 10));
//
//        trendingRepository.deleteAllInBatch();
//
//        List<TrendingBatch> trendingList = trendingKeywords.stream()
//                .map(dto -> new TrendingBatch(dto.getKeyword(), dto.getCount()))
//                .toList();
//
//        trendingRepository.saveAll(trendingList);
//    }
//
//    /**
//     * 1주일 이상 된 오래된 검색 로그 삭제
//     */
//    @Transactional
//    public void deleteOldSearchLogs() {
//        LocalDateTime aWeekAgo = LocalDateTime.now().minusWeeks(1);
//        searchLogRepository.deleteSearchLogByCreatedAtBefore(aWeekAgo);
//    }

}
