package com.example.springrider.domain.search.repository;


import com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto;
import com.example.springrider.domain.search.entity.QTrending;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TrendingQueryRepositoryImpl implements TrendingQueryRepository {
    private final JPAQueryFactory queryFactory;

    QTrending trending=QTrending.trending;
    @Override
    public List<SearchTrendingResponseDto> trendingKeyword(Long rank) {
        return queryFactory
                .select(Projections.constructor(
                        SearchTrendingResponseDto.class,
                        trending.id,trending.counter,trending.keyword
                ))
                .from(trending)
                .orderBy(trending.counter.desc())
                .limit(rank)
                .fetch();
    }
}
