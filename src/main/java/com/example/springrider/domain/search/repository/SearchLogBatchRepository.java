package com.example.springrider.domain.search.repository;

import com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto;
import com.example.springrider.domain.search.entity.SearchLogBatch;
import com.example.springrider.domain.search.entity.SearchLogId;
import com.example.springrider.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface SearchLogBatchRepository extends JpaRepository<SearchLogBatch, SearchLogId> {

    @Transactional
    @Modifying
    @Query(value = "INSERT IGNORE INTO search_batch (time, keyword, user_id)" +
            "VALUES (:time, :keyword, :userId)"
            , nativeQuery = true)
    void insertIgnore(Integer time, String keyword, Long userId);

}
