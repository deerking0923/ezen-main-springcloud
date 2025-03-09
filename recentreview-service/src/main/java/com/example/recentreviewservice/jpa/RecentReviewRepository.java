package com.example.recentreviewservice.jpa;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface RecentReviewRepository extends CrudRepository<RecentReviewEntity, Long> {
    // id 기준 내림차순으로 최신 5건만 조회
    List<RecentReviewEntity> findTop5ByOrderByIdDesc();
}
