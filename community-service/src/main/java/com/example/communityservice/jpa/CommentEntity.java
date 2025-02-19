package com.example.communityservice.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // userId: user-service에서 관리하는 사용자 식별자
    @Column(nullable = false)
    private String userId;

    // 게시글과 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDate createDate;

    @Column(nullable = false)
    private String content;
}
