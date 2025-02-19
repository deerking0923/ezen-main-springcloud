package com.example.communityservice.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseComment {
    private Long id;
    private Long postId;
    private Long userId;
    private LocalDate createDate;
    private String content;
}
