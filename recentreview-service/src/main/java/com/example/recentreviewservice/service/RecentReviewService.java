package com.example.recentreviewservice.service;

import com.example.recentreviewservice.jpa.RecentReviewEntity;

public interface RecentReviewService {
    Iterable<RecentReviewEntity> getAllReviews();
}
