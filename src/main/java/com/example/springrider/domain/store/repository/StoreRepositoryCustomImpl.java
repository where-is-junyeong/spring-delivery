package com.example.springrider.domain.store.repository;

import com.example.springrider.domain.menu.entity.QMenu;
import com.example.springrider.domain.store.entity.QStore;
import com.example.springrider.domain.store.entity.Store;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Store> searchByKeywordWithLimit(String keyword, Long offset, Long limit) {
        QStore store = QStore.store;
        QMenu menu = QMenu.menu;

        String likeKeyword = "%" + keyword + "%";

        return queryFactory
                .selectDistinct(store)
                .from(store)
                .leftJoin(store.menus,menu).fetchJoin()
                .where(
                        store.name.likeIgnoreCase(likeKeyword)
                                .or(menu.name.likeIgnoreCase(likeKeyword))
                )
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
