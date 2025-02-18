// src/main/java/com/example/booksearchservice/dto/BookReviewDto.java
package com.example.bookreviewservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookReviewDto {
    private Long id;
    private String isbn;
    private Long userId;
    private LocalDate createDate;
    private String content;
}
