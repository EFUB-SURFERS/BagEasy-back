package com.efub.bageasy.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
/*
{"memberId":"1",
"postTitle":"update",
"postContent":"쌉니다",
"price":"600"}
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PostUpdateRequestDto {
    private  String postTitle;
    private  String postContent;
    private Long price;

    private String school;




}
