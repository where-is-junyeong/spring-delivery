package com.example.springrider.domain.test.service;

import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SearchLogServiceTest {

    private final EntityManager entityManager;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_KEY = "search_logs";
    private static final String REDIS_RANKING_KEY = "search_rank";

    @Transactional
    public void batchInsertLog(List<String> keywords) {
        int batchSize = 1000;

        for (int i = 0; i < keywords.size(); i += batchSize) {
            int end = Math.min(i + batchSize, keywords.size());
            List<String> batch = keywords.subList(i, end);
            batch.forEach(this::enqueue);
        }
    }
    //기존에 기록 삭제
    public void resetRedisData() {
        redisTemplate.delete(REDIS_KEY);
        redisTemplate.delete(REDIS_RANKING_KEY);
    }

    // Redis 큐에 로그 쌓기
    public void enqueue(String keyword) {
        redisTemplate.opsForList().rightPush(REDIS_KEY, keyword);
        redisTemplate.opsForZSet().incrementScore(REDIS_RANKING_KEY, keyword, 1);
    }

    @Transactional
    public void batchInsertStore(List<String> keywords, User user) {
        int batchSize = 1000;

        for (int i = 0; i < keywords.size(); i++) {
            String keyword = keywords.get(i);

            Store store = Store.builder()
                    .name(keyword)
                    .address("서울시 강남구")
                    .category(keyword.contains("bhc") ? "치킨" : keyword.contains("파스타집") ? "양식" : "한식")
                    .description("테스트 가게 설명")
                    .openTime(LocalTime.parse("09:00"))
                    .closeTime(LocalTime.parse("21:00"))
                    .minOrderPrice(12000)
                    .status(StoreStatus.ACTIVE)
                    .user(user)
                    .build();

            entityManager.persist(store);

            // flush + clear every batchSize
            if ((i + 1) % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        // 마지막 flush (개수가 batchSize의 배수가 아닐 경우 대비)
        entityManager.flush();
        entityManager.clear();
    }


}
