package com.efub.bageasy.domain.chat.repository;

import com.efub.bageasy.domain.chat.dto.response.ChatRoomResponseDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.efub.bageasy.domain.chat.domain.QRoom.*;
import static com.efub.bageasy.domain.member.domain.QMember.*;

import com.efub.bageasy.domain.chat.domain.Room;


@Repository
@RequiredArgsConstructor
public class ChatQuerydslRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getChatRooms(Long memberId) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);

        return jpaQueryFactory.select(Projections.constructor(ChatRoomResponseDto.class,
                        room.roomId,
                        room.buyerId.as("createMember"),
                        room.sellerId.as("joinMember"),
                        room.postId,
                        Projections.constructor(ChatRoomResponseDto.Participant.class,
                                ExpressionUtils.as(
                                        JPAExpressions.select(member.nickname)
                                                .from(member)
                                                .where(member.memberId.eq(
                                                        new CaseBuilder()
                                                                .when(room.buyerId.eq(memberId)).then(room.sellerId)
                                                                .otherwise(room.buyerId)

                                                )),"nickname")
                                )))
                .from(room)
                .where(room.buyerId.eq(memberId).or(room.sellerId.eq(memberId)))
                .fetch();
    }


}
