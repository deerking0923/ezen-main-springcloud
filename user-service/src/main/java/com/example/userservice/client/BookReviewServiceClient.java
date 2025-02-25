package com.example.userservice.client;

import com.example.userservice.vo.ResponseReview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "bookreview-service")
public interface BookReviewServiceClient {

    @GetMapping("/bookreview-service/{userId}/reviews")
    List<ResponseReview> getReviews(@PathVariable("userId") String userId);
}
