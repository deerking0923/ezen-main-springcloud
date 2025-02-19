package com.example.communityservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentDto {
    private Long id;
    private Long postId;
    private Long userId;
    private LocalDate createDate; // DB에서 @CreationTimestamp로 자동 생성
    private String content;
}
