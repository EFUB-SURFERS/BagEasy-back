package com.efub.bageasy.domain.chat.domain;

import com.efub.bageasy.global.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Room extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long roomId;

    @Column(name = "member_id", nullable = false)
    private Long sellerId;

    @Column(name="buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Builder
    public Room(Long sellerId, Long buyerId, Long postId){
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.postId = postId;
    }

}
