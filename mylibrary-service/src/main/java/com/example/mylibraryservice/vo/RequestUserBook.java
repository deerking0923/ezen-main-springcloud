// src/main/java/com/example/mylibraryservice/vo/RequestUserBook.java
package com.example.mylibraryservice.vo;

import lombok.Data;

@Data
public class RequestUserBook {
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
