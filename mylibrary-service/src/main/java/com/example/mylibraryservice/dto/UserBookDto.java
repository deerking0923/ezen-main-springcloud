// src/main/java/com/example/mylibraryservice/dto/UserBookDto.java
package com.example.mylibraryservice.dto;

import lombok.Data;

@Data
public class UserBookDto {
    private Long id;                // DB PK
    private String userId;          // user-service의 userId(문자열)
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String pDate;
    private String description;
    private String thumbnail;
    private String personalReview;
    private String quotes;          // JSON 문자열
    private String startDate;
    private String endDate;
}
