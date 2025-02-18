// src/main/java/com/example/mylibraryservice/vo/ResponseUserBook.java
package com.example.mylibraryservice.vo;

import lombok.Data;

@Data
public class ResponseUserBook {
    private Long id;         // DB pk
    private String userId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String pDate;
    private String description;
    private String thumbnail;
    private String personalReview;
    private String quotes;
    private String startDate;
    private String endDate;
}
