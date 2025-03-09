package com.example.recentreviewservice.service;

import com.example.recentreviewservice.jpa.RecentReviewEntity;
import com.example.recentreviewservice.jpa.RecentReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class RecentReviewServiceImpl implements RecentReviewService {
    private final RecentReviewRepository reviewRepository;

    public RecentReviewServiceImpl(RecentReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Iterable<RecentReviewEntity> getAllReviews() {
        // DB에서 최신 5개만 조회
        return reviewRepository.findTop5ByOrderByIdDesc();
    }
}
