package com.example.springrider.domain.search.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTrending is a Querydsl query type for Trending
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTrending extends EntityPathBase<Trending> {

    private static final long serialVersionUID = 241593026L;

    public static final QTrending trending = new QTrending("trending");

    public final NumberPath<Long> counter = createNumber("counter", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    public QTrending(String variable) {
        super(Trending.class, forVariable(variable));
    }

    public QTrending(Path<? extends Trending> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTrending(PathMetadata metadata) {
        super(Trending.class, metadata);
    }

}

