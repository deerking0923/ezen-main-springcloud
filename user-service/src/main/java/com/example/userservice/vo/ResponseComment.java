package com.example.userservice.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseComment {
    private Long id;
    private String userId;
    private Long postId;
    private LocalDateTime createDate;
    private String content;

}
