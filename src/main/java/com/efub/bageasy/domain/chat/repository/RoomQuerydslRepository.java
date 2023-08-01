package com.efub.bageasy.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static com.efub.bageasy.domain.chat.domain.QRoom.*;

@Repository
@RequiredArgsConstructor
public class RoomQuerydslRepository {

    @PersistenceContext
    private EntityManager em;

    public Long countExistingRoom(Long buyerId, Long sellerId, Long postId){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

        Long fetchOne = jpaQueryFactory.select(room.roomId)
                .from(room)
                .where(room.buyerId.eq(buyerId), room.sellerId.eq(sellerId), room.postId.eq(postId))
                .fetchOne();

        return fetchOne;
    }
}
