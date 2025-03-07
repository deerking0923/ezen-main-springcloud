package com.example.userservice.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponsePost {
    private Long id;
    private String userId;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private int viewCount;
}
