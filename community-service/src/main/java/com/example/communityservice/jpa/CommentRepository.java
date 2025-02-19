package com.example.communityservice.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    // 특정 postId의 댓글 목록
    List<CommentEntity> findByPostId(Long postId);

    // 특정 사용자(userId)의 댓글 목록이 필요한 경우
    List<CommentEntity> findByUserId(Long userId);
}
