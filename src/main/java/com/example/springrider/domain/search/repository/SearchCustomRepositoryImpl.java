package com.example.springrider.domain.search.repository;

import com.example.springrider.domain.search.entity.Keyword;
import com.example.springrider.domain.search.entity.QKeyword;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class SearchCustomRepositoryImpl implements SearchCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Keyword> findTrendByOrderByCount(LocalDateTime time) {
        QKeyword keyword = QKeyword.keyword1;

        return jpaQueryFactory
                .selectFrom(keyword)
                .where(keyword.createdAt.after(time))
                .orderBy(keyword.count.desc())
                .limit(10)
                .fetch();
    }
}
