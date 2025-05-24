package com.example.springrider.config.cache;

import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CacheStrategy {

    private final RedisTemplate<String, String> template;

    public void evictSearchCacheByStore(String oldName, List<Menu> menus) {
        List<String> keywords = new ArrayList<>();
        keywords.add(oldName);

        for (Menu menu : menus) {
            keywords.add(menu.getName());
        }

        Set<String> keysToDelete = new HashSet<>();
        for (String keyword : keywords) {
            String pattern = "search::" + keyword + "*";  // 와일드카드 * 추가
            Set<String> matchedKeys = template.keys(pattern);
            if (matchedKeys != null && !matchedKeys.isEmpty()) {
                keysToDelete.addAll(matchedKeys);
            }
        }

        if (!keysToDelete.isEmpty()) {
            template.delete(keysToDelete);
        }
    }
}
