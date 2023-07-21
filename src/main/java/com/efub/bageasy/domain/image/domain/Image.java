package com.efub.bageasy.domain.image.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long imageId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "post_id", nullable = false)
    private Long postId;
}
