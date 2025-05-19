package com.example.springrider.domain.search.repository;

import com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto;
import com.example.springrider.domain.search.entity.SearchLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


//검색 기록이 아닌 검색한 결과의 기록
public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

    @Query("SELECT new com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto(MIN(sl.id), sl.keyword, COUNT(sl)) " +
            "FROM SearchLog sl " +
            "WHERE sl.createdAt >= :time " +
            "GROUP BY sl.keyword " +
            "ORDER BY COUNT(sl) DESC")
    Page<SearchTrendingResponseDto> findTrending(@Param("time") LocalDateTime time, Pageable pageable);

    void deleteSearchLogByCreatedAtBefore(LocalDateTime createdAtBefore);

}
