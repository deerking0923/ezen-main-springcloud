package com.example.communityservice.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // userId: user-service에서 관리되는 사용자 식별자
    @Column(nullable = false)
    private String userId;

    // 작성자 이름, 자동 세팅 (예: user-service에서 가져온 이름)
    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createdAt;

    // 조회수, 기본값 0
    @Column(nullable = false)
    private int viewCount = 0;

    // 게시글과 댓글의 1대 다 관계
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    // 조회수 증가 메서드 (게시글 상세 조회 시 호출)
    public void incrementViewCount() {
        this.viewCount++;
    }
}
