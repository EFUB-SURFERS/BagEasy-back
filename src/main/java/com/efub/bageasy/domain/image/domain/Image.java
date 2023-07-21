package com.efub.bageasy.domain.image.domain;

import com.efub.bageasy.domain.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long imageId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;


    public Image(String fileUrl , Post post){
        this.imageUrl=fileUrl;
        this.post = post;
    }

    public void initPost(final Post post){
        if(this.post==null){
            this.post=post;
        }
    }
}
