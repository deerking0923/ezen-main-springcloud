package com.example.communityservice.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseComment {
    private Long id;
    private String userId;
    private Long postId;
    private LocalDate createDate;
    private String content;
}
