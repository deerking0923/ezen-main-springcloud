package com.example.communityservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class CommentDto {
    private Long id;
    private String userId;    // 댓글 작성자
    private Long postId;      // 어떤 게시글에 작성된 댓글인지
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private String content;
}
