// src/main/java/com/example/booksearchservice/service/BookReviewService.java
package com.example.bookreviewservice.service;

import com.example.bookreviewservice.dto.BookReviewDto;

import java.util.List;

public interface BookReviewService {
    BookReviewDto createReview(BookReviewDto reviewDto);
    BookReviewDto updateReview(Long reviewId, BookReviewDto reviewDto);
    boolean deleteReview(Long reviewId, String userId);
    List<BookReviewDto> getReviewsByIsbn(String isbn);
    BookReviewDto getReview(Long reviewId);
}
