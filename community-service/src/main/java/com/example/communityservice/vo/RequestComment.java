package com.example.communityservice.vo;

import lombok.Data;

@Data
public class RequestComment {
    private String userId;
    private Long postId;
    private String content;
}
