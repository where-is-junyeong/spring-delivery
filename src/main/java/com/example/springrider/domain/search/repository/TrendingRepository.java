package com.example.springrider.domain.search.repository;

import com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto;
import com.example.springrider.domain.search.entity.Trending;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrendingRepository extends JpaRepository<Trending,Long>, TrendingQueryRepository{

    @Query("SELECT new com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto(t.id, t.keyword,t.counter) FROM Trending t")
    Page<SearchTrendingResponseDto> findTrending(Pageable pageable);
}
