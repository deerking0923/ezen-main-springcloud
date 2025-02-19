package com.example.communityservice.vo;

import lombok.Data;

@Data
public class RequestPost {
    private Long userId;
    private String title;
    private String content;
}
