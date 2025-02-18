// src/main/java/com/example/booksearchservice/jpa/BookReviewRepository.java
package com.example.bookreviewservice.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookReviewRepository extends CrudRepository<BookReviewEntity, Long> {
    // 특정 ISBN의 리뷰 목록
    List<BookReviewEntity> findByIsbn(String isbn);

    // 특정 사용자(userId)의 리뷰 목록
    List<BookReviewEntity> findByUserId(String userId);
}
