package com.example.communityservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostDto {
    private Long id;
    private String userId;     // 글 작성자
    private String title;
    private String content;
    private LocalDate createDate;
}
