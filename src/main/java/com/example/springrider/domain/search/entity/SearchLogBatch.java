package com.example.springrider.domain.search.entity;

import com.example.springrider.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "search_batch")
public class SearchLogBatch {

    @EmbeddedId
    private SearchLogId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SearchLogBatch(String keyword, Long userId){

        LocalDateTime now = LocalDateTime.now();

        int time = now.getYear()*1000000
                + now.getMonthValue()*10000
                + now.getDayOfMonth()*100
                + now.getHour();

        this.id = new SearchLogId(time, keyword, userId);
    }

}
