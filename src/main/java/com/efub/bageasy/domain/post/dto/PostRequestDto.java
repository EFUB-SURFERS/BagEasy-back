package com.efub.bageasy.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PostRequestDto {
    private String postTitle;
    //private Long sellerId;
    private Long price;
    private String postContent;
    private List<MultipartFile> images=new ArrayList<>();

}
