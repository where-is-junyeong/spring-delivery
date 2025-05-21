package com.example.springrider.domain.search.repository;

import com.example.springrider.domain.search.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SearchRepository extends JpaRepository <Keyword, Long>, SearchCustomRepository {


    Optional<Keyword> findByKeyword(String keyword);

    @Query("select k from Keyword k where k.createdAt >= :time order by k.count desc")
    List<Keyword> findTrendByOrderByCountDesc(LocalDateTime time);
}
