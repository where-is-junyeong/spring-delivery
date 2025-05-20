package com.example.springrider.domain.search.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Builder
@Entity
@Getter
@Table(name = "search")
@NoArgsConstructor
@AllArgsConstructor
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String keyword;

    @Column
    private Long count;

    @Column
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    public static Search of(String keyword, Long count) {
//        return new Search(keyword, count);
        return Search.builder()
            .keyword(keyword)
            .count(count)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void count() {
        if (this.count == null) {
            this.count = 0L;
        }
        this.count++;
    }
}
