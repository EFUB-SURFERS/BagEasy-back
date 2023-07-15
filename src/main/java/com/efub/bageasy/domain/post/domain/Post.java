package com.efub.bageasy.domain.post.domain;

import com.efub.bageasy.domain.image.domain.Image;
import com.efub.bageasy.domain.post.dto.PostUpdateRequestDto;
import com.efub.bageasy.global.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @Column(name = "is_sold", nullable = false)
    @ColumnDefault("false")
    private Boolean isSold;

    @Column
    private Long price;

    @Column(name = "school_id", nullable = false)
    private Long schoolId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "buyer_id")
    private Long buyerId;

    // 이미지
    /*
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Image> images;
     */

    @Transient
    private final List<Image> imageList = new ArrayList<>();

    @Builder
    public Post(Long postId, String title, String content, Boolean isSold, Long price,
                Long schoolId, Long memberId, Long buyerId, List<Image> images){
        this.postId=postId;
        this.title=title;
        this.content=content;
        this.isSold=isSold;
        this.price=price;
        this.schoolId=schoolId;
        this.memberId=memberId;
        this.buyerId=buyerId;
        //addImages(images);
    }

    public Post(String title, String content, Long price, Long memberId){
        this.title=title;
        this.content=content;
        this.price=price;
        this.memberId=memberId;
        this.isSold=false;
        this.schoolId= 1L;
    }

    // 양도글 수정
    public void update(PostUpdateRequestDto requestDto) {
       this.title=requestDto.getPostTitle();
       this.content=requestDto.getPostContent();
       this.price=requestDto.getPrice();

    }


    // 구매 확정
    public void updateIsSold(Long buyerId) {
        this.isSold= Boolean.valueOf("true");
        this.buyerId=buyerId;

    }
}
