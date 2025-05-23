package com.example.springrider.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // 각각의 캐시를 CaffeineCache로 생성
        CaffeineCache searchCache = new CaffeineCache("searchCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .maximumSize(1000)
                        .build());

        CaffeineCache searchCacheLog = new CaffeineCache("searchCacheLog",
                Caffeine.newBuilder()
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .maximumSize(500)
                        .build());

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        // setCaches는 한 번만 호출하고, 모든 캐시를 리스트로 넘겨야 함
        cacheManager.setCaches(List.of(searchCache, searchCacheLog));
        return cacheManager;
    }
}

