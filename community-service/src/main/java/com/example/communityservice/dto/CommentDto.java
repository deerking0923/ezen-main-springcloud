package com.example.communityservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentDto {
    private Long id;
    private String userId;    // 댓글 작성자
    private Long postId;      // 어떤 게시글에 작성된 댓글인지
    private LocalDate createDate;
    private String content;
}
