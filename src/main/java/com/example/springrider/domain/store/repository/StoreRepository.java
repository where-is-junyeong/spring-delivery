package com.example.springrider.domain.store.repository;

import com.example.springrider.global.exception.ExceptionCode;
import com.example.springrider.global.exception.InvalidRequestException;
import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.user.entity.User;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long>{

    long countByUser(User user); // 사장님이 등록한 가게 개수

    long countByUserAndStatus(User currentUser, StoreStatus storeStatus);

    List<Store> findAllByStatusNot(StoreStatus status);

    default Store findByIdOrElseThrow(Long storeId) {
        return findById(storeId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.STORE_NOT_FOUND));
    }
    //수정 필요


    List<Store> findByIdIn(List<Long> storeIds);

    @Query(value = """
            SELECT DISTINCT s.*
            FROM store s
            LEFT JOIN menu m ON m.store_id = s.id
            WHERE MATCH(s.name) AGAINST (:keyword IN BOOLEAN MODE)
               OR MATCH(m.name) AGAINST (:keyword IN BOOLEAN MODE)
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<Store> searchByKeywordNative(
            @Param("keyword") String keyword,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
    @Query(value = """
            SELECT COUNT(DISTINCT s.id)
            FROM store s
            LEFT JOIN menu m ON m.store_id = s.id
            WHERE MATCH(s.name) AGAINST (:keyword IN BOOLEAN MODE)
               OR MATCH(m.name) AGAINST (:keyword IN BOOLEAN MODE)
            """, nativeQuery = true)
    long countByKeywordNative(@Param("keyword") String keyword);
}
