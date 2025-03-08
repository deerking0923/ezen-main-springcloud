package com.example.recentreviewservice.jpa;

import org.springframework.data.repository.CrudRepository;

public interface RecentReviewRepository extends CrudRepository<RecentReviewEntity, Long> {
    // 필요한 추가 조회 메서드가 있다면 여기에 선언
}
