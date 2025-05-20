package com.example.springrider.domain.store.repository;

import ch.qos.logback.core.util.StringUtil;
import com.example.springrider.domain.menu.entity.QMenu;
import com.example.springrider.domain.store.dto.response.FindAllStoreResponseDto;
import com.example.springrider.domain.store.entity.QStore;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreQueryRepositoryImpl implements StoreQueryRepository{

    private final JPAQueryFactory queryFactory;
    private final QStore store = QStore.store;
    private final QMenu menu = QMenu.menu;

    public Page<FindAllStoreResponseDto> search(String keyword, Pageable pageable) {
        List<FindAllStoreResponseDto> stores = queryFactory
            .select(Projections.constructor(
                FindAllStoreResponseDto.class,
                store.id,
                store.name,
                store.address,
                store.category,
                store.description,
                store.minOrderPrice,
                store.openTime,
                store.closeTime
            ))
            .from(store)
            .leftJoin(store.menus, menu)
            .where(
                containsKeyword(keyword)
            )
            .distinct()
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(store.countDistinct())
            .from(store)
            .leftJoin(store.menus, menu)
            .where(
                containsKeyword(keyword)
            )
            .fetchOne();

        return new PageImpl<>(stores, pageable, total != null ? total : 0L);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (StringUtil.isNullOrEmpty(keyword)) {
            return null;
        }
        return store.name.containsIgnoreCase(keyword).or(menu.name.containsIgnoreCase(keyword));
    }

}
