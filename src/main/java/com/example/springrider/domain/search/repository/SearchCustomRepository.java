package com.example.springrider.domain.search.repository;

import com.example.springrider.domain.search.entity.Keyword;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchCustomRepository {

    List<Keyword> findTrendByOrderByCount(LocalDateTime time);
}
