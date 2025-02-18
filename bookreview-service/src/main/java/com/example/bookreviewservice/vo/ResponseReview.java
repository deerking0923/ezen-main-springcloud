// src/main/java/com/example/booksearchservice/vo/ResponseReview.java
package com.example.bookreviewservice.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseReview {
    private Long id;
    private String isbn;
    private Long userId;
    private LocalDate createDate;
    private String content;
}
