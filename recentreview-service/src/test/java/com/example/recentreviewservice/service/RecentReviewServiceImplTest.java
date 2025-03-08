package com.example.recentreviewservice.service;

import com.example.recentreviewservice.jpa.RecentReviewEntity;
import com.example.recentreviewservice.jpa.RecentReviewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecentReviewServiceImplTest {

    @Test
    void testGetAllReviews() {
        // 모의 객체(목) 생성
        RecentReviewRepository repository = Mockito.mock(RecentReviewRepository.class);
        
        // 샘플 데이터 준비
        RecentReviewEntity review1 = new RecentReviewEntity();
        review1.setIsbn("9781234567890");
        review1.setUserId("user1");
        review1.setContent("리뷰 내용 1");
        review1.setCreateDate(LocalDate.now());
        
        RecentReviewEntity review2 = new RecentReviewEntity();
        review2.setIsbn("9781234567891");
        review2.setUserId("user2");
        review2.setContent("리뷰 내용 2");
        review2.setCreateDate(LocalDate.now().minusDays(1));
        
        List<RecentReviewEntity> reviews = Arrays.asList(review1, review2);
        Mockito.when(repository.findAll()).thenReturn(reviews);
        
        RecentReviewServiceImpl service = new RecentReviewServiceImpl(repository);
        
        Iterable<RecentReviewEntity> result = service.getAllReviews();
        // 간단히 리뷰 개수가 예상과 같은지 확인
        int count = 0;
        for (RecentReviewEntity review : result) {
            count++;
        }
        assertEquals(2, count);
    }
}
