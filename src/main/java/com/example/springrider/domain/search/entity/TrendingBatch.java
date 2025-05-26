package com.example.springrider.domain.search.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class TrendingBatch {

    @EmbeddedId
    private TrendingBatchId id;

    Integer point;

}
