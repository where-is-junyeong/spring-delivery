package com.example.springrider.domain.test.controller;

import com.example.springrider.config.security.CustomUserPrincipal;
import com.example.springrider.domain.test.service.SearchLogServiceTest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class DummyLogController {

    private final SearchLogServiceTest searchLogService;

    @PostMapping("/generate-logs")
    public String generateDummyLog(@RequestParam(defaultValue = "50000") int count) {

        List<String> candidates = getStoresAndMenus();
        List<String> keywords = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            int idx = (int) (Math.random() * candidates.size());
            keywords.add(candidates.get(idx));

        }
        searchLogService.batchInsertLog(keywords);

        return count + "개의 더미 Log 데이터를 배치로 생성했습니다.";
    }
    @PostMapping("/generate-stores")
    public String generateDummyStore(@RequestParam(defaultValue = "50000") int count,
                                     @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {

        List<String> storeName = List.of("김밥천국", "파스타집", "bhc");
        List<String> keywords = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            int idx = (int) (Math.random() * storeName.size());
            String keyword = storeName.get(idx) + i;
            keywords.add(keyword);

        }
        searchLogService.batchInsertStore(keywords,userPrincipal.getUser());

        return count + "개의 더미 가게 데이터를 배치로 생성했습니다.";
    }
    @DeleteMapping("/reset")
    public String resetRedisLogs() {
        searchLogService.resetRedisData();
        return "✅ Redis 검색 로그 및 랭킹 초기화 완료!";
    }

    public static List<String> getStoresAndMenus() {
        List<String> result = new ArrayList<>();

        // 가게 이름 추가
        result.add("김밥천국");
        result.add("파스타집");
        result.add("bhc");

        // 김밥천국 메뉴 추가
        result.add("원조김밥");
        result.add("참치김밥");
        result.add("돈까스");

        // 파스타집 메뉴 추가
        result.add("알리오 올리오");
        result.add("토마토 스파게티");
        result.add("봉골레 파스타");

        // bhc 메뉴 추가
        result.add("후라이드");
        result.add("양념");
        result.add("반반");

        return result;
    }
}
