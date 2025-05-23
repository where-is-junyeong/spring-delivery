package com.example.springrider.config.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class CacheStrategy {
    @CacheEvict(value = "searchCache", allEntries = true)
    public void evictAllSearchCache() {

        // 캐시 전부 삭제
    }

}
