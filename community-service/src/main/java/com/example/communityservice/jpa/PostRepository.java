package com.example.communityservice.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends CrudRepository<PostEntity, Long> {
    // 기존 메서드
    List<PostEntity> findByUserId(String userId);

    // 댓글도 함께 가져오는 fetch join 메서드
    @Query("select distinct p from PostEntity p left join fetch p.comments where p.userId = :userId")
    List<PostEntity> findByUserIdWithComments(@Param("userId") String userId);
}
