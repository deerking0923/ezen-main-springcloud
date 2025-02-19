package com.example.communityservice.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<PostEntity, Long> {
    // 특정 사용자(userId)가 작성한 게시글 목록
    List<PostEntity> findByUserId(String userId);
}
