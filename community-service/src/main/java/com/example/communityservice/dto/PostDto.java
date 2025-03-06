package com.example.communityservice.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class PostDto {
    private Long id;
    private String userId;
    private String author;   // 작성자 이름
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private int viewCount;
}
