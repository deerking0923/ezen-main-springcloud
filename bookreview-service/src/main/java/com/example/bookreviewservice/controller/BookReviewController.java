package com.example.bookreviewservice.controller;

import com.example.bookreviewservice.dto.BookReviewDto;
import com.example.bookreviewservice.service.BookReviewService;
import com.example.bookreviewservice.vo.RequestReview;
import com.example.bookreviewservice.vo.ResponseReview;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.bookreviewservice.messagequeue.KafkaProducer;
import com.example.bookreviewservice.messagequeue.BookReviewProducer;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookreview-service")
@Slf4j
public class BookReviewController {

    private final Environment env;
    private final BookReviewService bookReviewService;
    private final KafkaProducer kafkaProducer;
    private final com.example.bookreviewservice.messagequeue.BookReviewProducer bookReviewProducer;

    @Autowired
    public BookReviewController(Environment env, 
                                  BookReviewService bookReviewService,
                                  KafkaProducer kafkaProducer, 
                                  com.example.bookreviewservice.messagequeue.BookReviewProducer bookReviewProducer) {
        this.env = env;
        this.bookReviewService = bookReviewService;
        this.kafkaProducer = kafkaProducer;
        this.bookReviewProducer = bookReviewProducer;
    }

    @GetMapping("/health-check")
    public String status() {
        return String.format("BookReview Service is working on LOCAL PORT %s (SERVER PORT %s)",
                env.getProperty("local.server.port"),
                env.getProperty("server.port"));
    }

    // 1) 리뷰 생성: POST /bookreview-service/{userId}/reviews
    @PostMapping("/{userId}/reviews")
    public ResponseEntity<ResponseReview> createReview(@PathVariable("userId") String userId,
                                                       @RequestBody RequestReview reviewDetails) {
        log.info("Creating review for userId: {}", userId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        BookReviewDto reviewDto = new BookReviewDto();
        reviewDto.setIsbn(reviewDetails.getIsbn());
        reviewDto.setContent(reviewDetails.getContent());
        reviewDto.setUserId(userId); // userId를 문자열로 처리

        BookReviewDto created = bookReviewService.createReview(reviewDto);
        ResponseReview responseReview = mapper.map(created, ResponseReview.class);

        // Kafka 메시지 전송
        kafkaProducer.send("review-topic", created);
        bookReviewProducer.send("review-topic-struct", created);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseReview);
    }

    // 2) 리뷰 수정: PUT /bookreview-service/{userId}/reviews/{reviewId}
    @PutMapping("/{userId}/reviews/{reviewId}")
    public ResponseEntity<ResponseReview> updateReview(@PathVariable("userId") String userId,
                                                       @PathVariable("reviewId") Long reviewId,
                                                       @RequestBody RequestReview reviewDetails) {
        log.info("Updating review {} for userId: {}", reviewId, userId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        BookReviewDto dto = new BookReviewDto();
        dto.setUserId(userId);
        dto.setContent(reviewDetails.getContent());

        BookReviewDto updated = bookReviewService.updateReview(reviewId, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        ResponseReview responseReview = mapper.map(updated, ResponseReview.class);
        return ResponseEntity.ok(responseReview);
    }

    // 3) 리뷰 삭제: DELETE /bookreview-service/{userId}/reviews/{reviewId}
    @DeleteMapping("/{userId}/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable("userId") String userId,
                                          @PathVariable("reviewId") Long reviewId) {
        log.info("Deleting review {} for userId: {}", reviewId, userId);
        boolean deleted = bookReviewService.deleteReview(reviewId, userId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    // 4) 특정 ISBN의 리뷰 목록 조회: GET /bookreview-service/reviews/isbn/{isbn}
    @GetMapping("/reviews/isbn/{isbn}")
    public ResponseEntity<List<ResponseReview>> getReviewsByIsbn(@PathVariable("isbn") String isbn) {
        List<BookReviewDto> dtos = bookReviewService.getReviewsByIsbn(isbn);
        ModelMapper mapper = new ModelMapper();
        List<ResponseReview> result = new ArrayList<>();
        dtos.forEach(dto -> result.add(mapper.map(dto, ResponseReview.class)));
        return ResponseEntity.ok(result);
    }

    // 5) 특정 리뷰 단건 조회: GET /bookreview-service/reviews/{reviewId}
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseReview> getReview(@PathVariable("reviewId") Long reviewId) {
        BookReviewDto dto = bookReviewService.getReview(reviewId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        ModelMapper mapper = new ModelMapper();
        ResponseReview res = mapper.map(dto, ResponseReview.class);
        return ResponseEntity.ok(res);
    }
}
