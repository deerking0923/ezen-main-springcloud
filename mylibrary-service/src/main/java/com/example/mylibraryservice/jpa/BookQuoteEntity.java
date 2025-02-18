package com.example.mylibraryservice.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "book_quotes")
public class BookQuoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user_books 테이블과 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_book_id", nullable = false)
    private UserBookEntity userBook;

    @Column(name = "page_number", length = 20)
    private String pageNumber;

    @Column(name = "quote_text", columnDefinition = "TEXT")
    private String quoteText;
}
