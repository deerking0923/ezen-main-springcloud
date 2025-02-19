package com.example.communityservice.jpa;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<PostEntity, Long> {
    // 필요 시 추가 쿼리 메서드
}
