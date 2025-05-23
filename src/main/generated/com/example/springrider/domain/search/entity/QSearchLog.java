package com.example.springrider.domain.search.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSearchLog is a Querydsl query type for SearchLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSearchLog extends EntityPathBase<SearchLog> {

    private static final long serialVersionUID = 383689343L;

    public static final QSearchLog searchLog = new QSearchLog("searchLog");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    public QSearchLog(String variable) {
        super(SearchLog.class, forVariable(variable));
    }

    public QSearchLog(Path<? extends SearchLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSearchLog(PathMetadata metadata) {
        super(SearchLog.class, metadata);
    }

}

