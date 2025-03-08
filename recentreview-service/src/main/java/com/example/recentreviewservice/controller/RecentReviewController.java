package com.example.recentreviewservice.controller;

import com.example.recentreviewservice.jpa.RecentReviewEntity;
import com.example.recentreviewservice.service.RecentReviewService;
import com.example.recentreviewservice.vo.ResponseReview;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recentreview-service")
public class RecentReviewController {

    private final Environment env;
    private final RecentReviewService recentReviewService;
    private final DiscoveryClient discoveryClient;

    @Autowired
    public RecentReviewController(Environment env, RecentReviewService recentReviewService, DiscoveryClient discoveryClient) {
        this.env = env;
        this.recentReviewService = recentReviewService;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in RecentReview Service on LOCAL PORT %s (SERVER PORT %s)",
                env.getProperty("local.server.port"),
                env.getProperty("server.port"));
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ResponseReview>> getReviews() {
        Iterable<RecentReviewEntity> reviewEntities = recentReviewService.getAllReviews();
        List<ResponseReview> result = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        reviewEntities.forEach(entity -> {
            result.add(mapper.map(entity, ResponseReview.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
