package com.example.communityservice.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    // 특정 게시글의 댓글
    List<CommentEntity> findByPostId(Long postId);

    // 특정 사용자(userId)가 작성한 댓글
    List<CommentEntity> findByUserId(String userId);
}
