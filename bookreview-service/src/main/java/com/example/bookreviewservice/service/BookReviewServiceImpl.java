package com.example.bookreviewservice.service;

import com.example.bookreviewservice.dto.BookReviewDto;
import com.example.bookreviewservice.jpa.BookReviewEntity;
import com.example.bookreviewservice.jpa.BookReviewRepository;
import com.example.bookreviewservice.messagequeue.KafkaProducer;
import com.example.bookreviewservice.messagequeue.BookReviewProducer;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookReviewServiceImpl implements BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final KafkaProducer kafkaProducer;
    private final BookReviewProducer bookReviewProducer;

    public BookReviewServiceImpl(BookReviewRepository bookReviewRepository,
                                 KafkaProducer kafkaProducer,
                                 BookReviewProducer bookReviewProducer) {
        this.bookReviewRepository = bookReviewRepository;
        this.kafkaProducer = kafkaProducer;
        this.bookReviewProducer = bookReviewProducer;
    }

    @Override
    public BookReviewDto createReview(BookReviewDto reviewDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        BookReviewEntity entity = mapper.map(reviewDto, BookReviewEntity.class);
        bookReviewRepository.save(entity);

        BookReviewDto createdReview = mapper.map(entity, BookReviewDto.class);

        // Kafka 전송
        kafkaProducer.send("review-topic", createdReview);
        bookReviewProducer.send("review-topic-struct", createdReview);

        return createdReview;
    }

    @Override
    public BookReviewDto updateReview(Long reviewId, BookReviewDto reviewDto) {
        Optional<BookReviewEntity> optionalEntity = bookReviewRepository.findById(reviewId);
        if (optionalEntity.isEmpty()) {
            return null;
        }
        BookReviewEntity entity = optionalEntity.get();
        // 작성자(userId) 체크 로직 필요 시 추가

        entity.setContent(reviewDto.getContent());
        bookReviewRepository.save(entity);

        ModelMapper mapper = new ModelMapper();
        BookReviewDto updatedReview = mapper.map(entity, BookReviewDto.class);

        // Kafka 전송 (수정 이벤트)
        kafkaProducer.send("review-topic", updatedReview);
        bookReviewProducer.send("review-topic-struct-update", updatedReview);

        return updatedReview;
    }

    @Override
    public boolean deleteReview(Long reviewId, String userId) {
        Optional<BookReviewEntity> optionalEntity = bookReviewRepository.findById(reviewId);
        if (optionalEntity.isEmpty()) return false;

        BookReviewEntity entity = optionalEntity.get();
        // 작성자(userId) 체크
        if (!entity.getUserId().equals(userId)) {
            return false; // 권한 없음
        }
        bookReviewRepository.delete(entity);

        BookReviewDto dto = new BookReviewDto();
        dto.setId(reviewId);
        dto.setUserId(userId);
        kafkaProducer.send("review-topic-delete", dto);
        bookReviewProducer.send("review-topic-struct-delete", dto);

        return true;
    }

    @Override
    public List<BookReviewDto> getReviewsByIsbn(String isbn) {
        List<BookReviewEntity> entities = bookReviewRepository.findByIsbn(isbn);
        List<BookReviewDto> dtos = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        for (BookReviewEntity e : entities) {
            dtos.add(mapper.map(e, BookReviewDto.class));
        }
        return dtos;
    }

    @Override
    public BookReviewDto getReview(Long reviewId) {
        Optional<BookReviewEntity> entityOpt = bookReviewRepository.findById(reviewId);
        if (entityOpt.isEmpty()) {
            return null;
        }
        ModelMapper mapper = new ModelMapper();
        return mapper.map(entityOpt.get(), BookReviewDto.class);
    }

    @Override
    public List<BookReviewDto> getReviewsByUserId(String userId) {
        List<BookReviewEntity> entities = bookReviewRepository.findByUserId(userId);
        List<BookReviewDto> dtos = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        for (BookReviewEntity e : entities) {
            dtos.add(mapper.map(e, BookReviewDto.class));
        }
        return dtos;
    }
}
