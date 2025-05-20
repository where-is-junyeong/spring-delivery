package com.example.springrider.domain.search.repository;

import com.example.springrider.domain.search.dto.response.TrendingKeywordResponseDto;
import com.example.springrider.domain.search.entity.QSearch;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SearchQueryRepositoryImpl implements SearchQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QSearch search = QSearch.search;

    public List<TrendingKeywordResponseDto> trendingKeyword(Long rank) {
        return queryFactory
            .select(Projections.constructor(
                TrendingKeywordResponseDto.class,
                search.count,
                search.keyword
            ))
            .from(search)
            .orderBy(search.count.desc())
            .limit(rank)
            .fetch();
    }
}
