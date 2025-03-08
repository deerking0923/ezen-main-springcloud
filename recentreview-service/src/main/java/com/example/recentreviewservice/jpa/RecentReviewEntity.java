package com.example.recentreviewservice.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "recent_reviews")
public class RecentReviewEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // DB에서 자동 생성 (payload의 review_id는 참고용)

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // payload에서 전달받은 생성일을 저장 (DB에서 직접 생성하지 않음)
    @Column(nullable = false)
    private LocalDate createDate;
}
