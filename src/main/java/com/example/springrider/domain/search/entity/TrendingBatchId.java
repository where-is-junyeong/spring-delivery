package com.example.springrider.domain.search.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TrendingBatchId implements Serializable {

    @CreatedDate
    Integer time; // YYYYMMDDHH
    @Column(length = 100)
    String keyword;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrendingBatchId that)) return false;

        return Objects.equals(time, that.time) &&
                Objects.equals(keyword, that.keyword);
    }

    @Override
    public int hashCode(){
        return Objects.hash(time, keyword);
    }

}
