package com.example.communityservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {
    private Long comment_id;  // DB id
    private Long post_id;
    private String user_id;
    private String content;
}
