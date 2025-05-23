package com.example.springrider.domain.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = 1614042875L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStore store = new QStore("store");

    public final com.example.springrider.domain.common.entity.QBaseEntity _super = new com.example.springrider.domain.common.entity.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final StringPath category = createString("category");

    public final TimePath<java.time.LocalTime> closeTime = createTime("closeTime", java.time.LocalTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final ListPath<com.example.springrider.domain.menu.entity.Menu, com.example.springrider.domain.menu.entity.QMenu> menus = this.<com.example.springrider.domain.menu.entity.Menu, com.example.springrider.domain.menu.entity.QMenu>createList("menus", com.example.springrider.domain.menu.entity.Menu.class, com.example.springrider.domain.menu.entity.QMenu.class, PathInits.DIRECT2);

    public final NumberPath<Integer> minOrderPrice = createNumber("minOrderPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final TimePath<java.time.LocalTime> openTime = createTime("openTime", java.time.LocalTime.class);

    public final EnumPath<com.example.springrider.domain.store.enums.StoreStatus> status = createEnum("status", com.example.springrider.domain.store.enums.StoreStatus.class);

    public final com.example.springrider.domain.user.entity.QUser user;

    public QStore(String variable) {
        this(Store.class, forVariable(variable), INITS);
    }

    public QStore(Path<? extends Store> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStore(PathMetadata metadata, PathInits inits) {
        this(Store.class, metadata, inits);
    }

    public QStore(Class<? extends Store> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.springrider.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

