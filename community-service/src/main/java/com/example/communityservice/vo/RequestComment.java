package com.example.communityservice.vo;

import lombok.Data;

@Data
public class RequestComment {
    private Long postId;
    private String content;
}
