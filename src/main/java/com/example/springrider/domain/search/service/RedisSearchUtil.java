package com.example.springrider.domain.search.service;

import com.example.springrider.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedisSearchUtil {

    private final RedisTemplate<String, String> redisTemplate;

    public List<Long> getStoreIds(String keyword, long start, long end) {
        String redisKey = getKey(keyword);
        Set<String> storeIdStrings = redisTemplate.opsForZSet().reverseRange(redisKey, start, end);
        redisTemplate.expire(redisKey, Duration.ofHours(24));

        if (storeIdStrings == null || storeIdStrings.isEmpty()) return Collections.emptyList();
        return storeIdStrings.stream().map(Long::valueOf).toList();
    }

    public long getTotal(String keyword) {
        String redisKey = getKey(keyword);
        return Optional.ofNullable(redisTemplate.opsForZSet().zCard(redisKey)).orElse(0L);
    }

    public void updateIndex(String keyword, List<Store> stores) {
        String redisKey = getKey(keyword);
        //추후 인기순(좋아요 순으로 변경예정)
        Set<ZSetOperations.TypedTuple<String>> entries = stores.stream()
                .map(store -> new DefaultTypedTuple<>(store.getId().toString(), (double) System.currentTimeMillis()))
                .collect(Collectors.toSet());

        redisTemplate.opsForZSet().add(redisKey, entries);
        redisTemplate.expire(redisKey, Duration.ofHours(24));
    }

    private String getKey(String keyword) {
        return "search::" + keyword;
    }
}
