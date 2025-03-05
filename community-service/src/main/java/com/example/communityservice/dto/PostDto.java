package com.example.communityservice.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PostDto {
    private Long id;
    private String userId;
    private String author;   // 작성자 이름
    private String title;
    private String content;
    private LocalDate createDate;
    private int viewCount;
}
