package com.efub.bageasy.domain.notice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotice is a Querydsl query type for Notice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotice extends EntityPathBase<Notice> {

    private static final long serialVersionUID = 76416159L;

    public static final QNotice notice = new QNotice("notice");

    public final com.efub.bageasy.global.QBaseTimeEntity _super = new com.efub.bageasy.global.QBaseTimeEntity(this);

    public final NumberPath<Long> commentId = createNumber("commentId", Long.class);

    public final NumberPath<Long> contentId = createNumber("contentId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath isChecked = createBoolean("isChecked");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Long> noticeId = createNumber("noticeId", Long.class);

    public final StringPath noticeType = createString("noticeType");

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final NumberPath<Long> postWriterId = createNumber("postWriterId", Long.class);

    public final NumberPath<Long> senderId = createNumber("senderId", Long.class);

    public final NumberPath<Long> targetId = createNumber("targetId", Long.class);

    public QNotice(String variable) {
        super(Notice.class, forVariable(variable));
    }

    public QNotice(Path<? extends Notice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotice(PathMetadata metadata) {
        super(Notice.class, metadata);
    }

}

