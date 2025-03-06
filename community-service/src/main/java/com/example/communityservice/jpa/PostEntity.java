package com.example.communityservice.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 작성자 ID (user-service에서 관리)
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private String content;

    // 생성 시 자동으로 현재 시각 설정 (DB 컬럼: create_date)
    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private int viewCount = 0;

    // 게시글과 댓글의 1대 다 관계
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CommentEntity> comments;

    public void incrementViewCount() {
        this.viewCount++;
    }
}
