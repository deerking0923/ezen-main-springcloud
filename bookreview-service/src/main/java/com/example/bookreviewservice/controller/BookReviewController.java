// src/main/java/com/example/booksearchservice/controller/BookReviewController.java
package com.example.bookreviewservice.controller;

import com.example.bookreviewservice.dto.BookReviewDto;
import com.example.bookreviewservice.service.BookReviewService;
import com.example.bookreviewservice.vo.RequestReview;
import com.example.bookreviewservice.vo.ResponseReview;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/booksearch-service")
@Slf4j
public class BookReviewController {

    private final BookReviewService bookReviewService;

    public BookReviewController(BookReviewService bookReviewService) {
        this.bookReviewService = bookReviewService;
    }

    // 1) 리뷰 생성
    @PostMapping("/reviews")
    public ResponseEntity<ResponseReview> createReview(
            @RequestBody RequestReview requestReview,
            @RequestHeader("userId") Long userId
    ) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        BookReviewDto reviewDto = new BookReviewDto();
        reviewDto.setIsbn(requestReview.getIsbn());
        reviewDto.setContent(requestReview.getContent());
        reviewDto.setUserId(userId);

        BookReviewDto created = bookReviewService.createReview(reviewDto);
        ResponseReview res = mapper.map(created, ResponseReview.class);
        return ResponseEntity.ok(res);
    }

    // 2) 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseReview> updateReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestBody RequestReview requestReview,
            @RequestHeader("userId") Long userId
    ) {
        // userId를 비교해서 본인 것만 수정하도록 구현 가능
        BookReviewDto dto = new BookReviewDto();
        dto.setUserId(userId);
        dto.setContent(requestReview.getContent());

        BookReviewDto updated = bookReviewService.updateReview(reviewId, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        ModelMapper mapper = new ModelMapper();
        ResponseReview res = mapper.map(updated, ResponseReview.class);
        return ResponseEntity.ok(res);
    }

    // 3) 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestHeader("userId") Long userId
    ) {
        boolean deleted = bookReviewService.deleteReview(reviewId, userId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    // 4) 특정 ISBN 리뷰 목록 조회
    @GetMapping("/reviews/isbn/{isbn}")
    public ResponseEntity<List<ResponseReview>> getReviewsByIsbn(@PathVariable("isbn") String isbn) {
        List<BookReviewDto> dtos = bookReviewService.getReviewsByIsbn(isbn);
        ModelMapper mapper = new ModelMapper();
        List<ResponseReview> result = dtos.stream()
                .map(dto -> mapper.map(dto, ResponseReview.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // 5) 특정 리뷰 단건 조회
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
