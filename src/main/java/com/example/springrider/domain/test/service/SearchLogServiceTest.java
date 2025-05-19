package com.example.springrider.domain.test.service;

import com.example.springrider.domain.search.dto.response.SearchTrendingResponseDto;
import com.example.springrider.domain.search.entity.SearchLog;
import com.example.springrider.domain.search.repository.SearchLogRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchLogServiceTest {

    private final EntityManager entityManager;

    @Transactional
    public void batchInsert(List<String> keywords) {
        int batchSize = 1000;
        for (int i = 0; i < keywords.size(); i++) {
            SearchLog searchLog = new SearchLog(keywords.get(i));
            entityManager.persist(searchLog);

            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }
}

