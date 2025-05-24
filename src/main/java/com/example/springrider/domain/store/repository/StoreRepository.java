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

public interface StoreRepository extends JpaRepository<Store, Long>,StoreRepositoryCustom {

    long countByUser(User user); // 사장님이 등록한 가게 개수

    long countByUserAndStatus(User currentUser, StoreStatus storeStatus);

    List<Store> findAllByStatusNot(StoreStatus status);

    default Store findByIdOrElseThrow(Long storeId) {
        return findById(storeId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.STORE_NOT_FOUND));
    }
    //수정 필요
    @EntityGraph(attributePaths = "menus")
    @Query("SELECT s FROM Store s WHERE s.name LIKE %:keyword% OR EXISTS (SELECT m FROM Menu m WHERE m.store = s AND m.name LIKE %:keyword%)")
    Page<Store> searchByKeywordPaged(@Param("keyword") String keyword, Pageable pageable);


    @EntityGraph(attributePaths = "menus")
    @Query("SELECT s FROM Store s WHERE s.name LIKE %:keyword% OR EXISTS (SELECT m FROM Menu m WHERE m.store = s AND m.name LIKE %:keyword%)")
    List<Store> searchByKeywordAll(@Param("keyword") String keyword, Pageable pageable);


    List<Store> findByIdIn(List<Long> storeIds);
}
