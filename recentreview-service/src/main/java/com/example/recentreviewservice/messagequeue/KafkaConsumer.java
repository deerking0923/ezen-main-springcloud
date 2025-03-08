package com.example.recentreviewservice.messagequeue;

import com.example.recentreviewservice.dto.Payload;
import com.example.recentreviewservice.jpa.RecentReviewEntity;
import com.example.recentreviewservice.jpa.RecentReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {
    private final RecentReviewRepository repository;
    private final ObjectMapper objectMapper;

    public KafkaConsumer(RecentReviewRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "review-topic", groupId = "recent-review-group")
    public void consumeReview(String message) {
        log.info("Received message: {}", message);
        try {
            Payload payload = objectMapper.readValue(message, Payload.class);
            RecentReviewEntity entity = new RecentReviewEntity();
            entity.setIsbn(payload.getIsbn());
            entity.setUserId(payload.getUserId());
            entity.setContent(payload.getContent());
            entity.setCreateDate(payload.getCreateDate());
            repository.save(entity);
        } catch (Exception e) {
            log.error("Error processing Kafka message: ", e);
        }
    }
}
