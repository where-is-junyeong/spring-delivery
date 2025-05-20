package com.example.springrider.domain.search.repository;

import com.example.springrider.domain.search.entity.Search;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long> {

    Optional<Search> findByKeyword(String keyword);
}
