package com.example.communityservice.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글 작성자 (user-service에서 관리)
    @Column(nullable = false)
    private String userId;

    // 다대일 관계: 댓글은 하나의 게시글에 속함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private PostEntity post;

    // 생성 시 자동으로 현재 시각 설정 (DB 컬럼: create_date)
    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private String content;
}
