package com.example.springrider.domain.search.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    private Long count;

    @CreatedDate
    private LocalDateTime createdAt;

    public Keyword(String keyword){
        this.keyword = keyword;
        this.count = 1L;
    }
    public Keyword(String keyword, Long count){
        this.keyword = keyword;
        this.count = count;
    }

    public void increaseCount(){
        this.count++;
    }
}
