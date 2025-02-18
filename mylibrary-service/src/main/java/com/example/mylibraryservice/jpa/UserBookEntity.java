package com.example.mylibraryservice.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="user_books")
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
    private String quotes;         // JSON or String
    private String startDate;
    private String endDate;
}
