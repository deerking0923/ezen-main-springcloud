package com.example.mylibraryservice.jpa;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "user_books")
public class UserBookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String userId;         // user-service의 userId(문자열)

    @Column(nullable = false, length = 20)
    private String isbn;

    private String title;
    private String author;
    private String publisher;
    private String pDate;
    private String description;
    private String thumbnail;
    private String personalReview;
    
    // 기존 String quotes 필드를 제거하고, BookQuoteEntity와의 관계를 설정합니다.
    @OneToMany(mappedBy = "userBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookQuoteEntity> quotes;
    
    private String startDate;
    private String endDate;
}
