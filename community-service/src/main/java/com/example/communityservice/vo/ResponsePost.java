package com.example.communityservice.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponsePost {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private LocalDate createDate;
}
