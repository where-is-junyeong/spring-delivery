package com.example.springrider.domain.store.repository;

import com.example.springrider.domain.menu.entity.QMenu;
import com.example.springrider.domain.store.entity.QStore;
import com.example.springrider.domain.store.entity.Store;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private BooleanExpression storeFullTextMatch(String keyword) {
        return Expressions.booleanTemplate(
                "MATCH({0}) AGAINST ({1} IN BOOLEAN MODE)", QStore.store.name, keyword
        );
    }

    private BooleanExpression menuFullTextMatch(String keyword) {
        return Expressions.booleanTemplate(
                "EXISTS (SELECT 1 FROM menu m WHERE m.store_id = {0} AND MATCH(m.name) AGAINST ({1} IN BOOLEAN MODE))",
                QStore.store.id, keyword
        );
    }

    @Override
    public List<Store> searchByKeywordWithLimit(String keyword, Long offset, Long limit) {
        QStore store = QStore.store;
        QMenu menu = QMenu.menu;

        return queryFactory
                .selectDistinct(store)
                .from(store)
                .where(storeFullTextMatch(keyword).or(menuFullTextMatch(keyword)))
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public Page<Store> searchByKeywordPaged(String keyword, Pageable pageable) {
        QStore store = QStore.store;
        QMenu menu = QMenu.menu;

        BooleanExpression predicate = storeFullTextMatch(keyword).or(menuFullTextMatch(keyword));

        // 실제 결과 조회
        List<Store> results = queryFactory
                .selectFrom(store)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // countQuery는 fetchJoin 없이 별도로 작성 (중복 방지 및 성능 고려)
        Long total = queryFactory
                .select(store.countDistinct())
                .from(store)
                .leftJoin(store.menus, menu)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0L);
    }
}
