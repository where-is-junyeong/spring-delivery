package com.example.springrider.domain.search.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

//검색 기록이 아닌 검색한 결과의 기록
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SearchLog {

    //식별 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //키워드
    String keyword;

    //생성일
    @CreatedDate
    private LocalDateTime createdAt;

    public SearchLog(String keyword) {
        this.keyword=keyword;
    }
}
