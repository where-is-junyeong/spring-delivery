package com.example.springrider.domain.test.controller;
import com.example.springrider.domain.test.service.SearchLogServiceTest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class DummyLogController {

    private final SearchLogServiceTest searchLogService;

    @PostMapping("/generate-logs")
    public String generateDummyLog(@RequestParam(defaultValue = "100000") int count) {

        List<String> keywords = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int randomNum = 1 + (int)(Math.random() * 6);  // 1 ~ 6 사이 숫자 생성
            keywords.add(String.valueOf(randomNum));
        }

        searchLogService.batchInsert(keywords);

        return count + "개의 더미 Log 데이터를 배치로 생성했습니다.";
    }
}
