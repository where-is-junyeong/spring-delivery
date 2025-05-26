package com.example.springrider.domain.search.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SearchLogId implements Serializable {

    Integer time; // YYYYMMDDHH

    @Column(length = 100)
    String keyword;

    Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchLogId that)) return false;

        return Objects.equals(time, that.time) &&
                Objects.equals(keyword, that.keyword) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(time, keyword, userId);
    }
}
