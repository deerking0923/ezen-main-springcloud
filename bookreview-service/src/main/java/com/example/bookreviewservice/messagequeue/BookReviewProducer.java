// src/main/java/com/example/booksearchservice/messagequeue/BookReviewProducer.java
package com.example.bookreviewservice.messagequeue;

import com.example.bookreviewservice.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class BookReviewProducer {

    // 실제 KafkaTemplate<String, String>을 주입받아 send할 수도 있음
    // 여기서는 예시로 로그만 찍는 구조

    public BookReviewDto send(String topic, BookReviewDto dto) {
        // 1) fields 정의
        List<Field> fields = Arrays.asList(
                new Field("int64", true, "review_id"),
                new Field("string", true, "isbn"),
                new Field("int64", true, "user_id"),
                new Field("string", true, "content")
        );

        Schema schema = Schema.builder()
                .type("struct")
                .fields(fields)
                .optional(false)
                .name("reviews")
                .build();

        Payload payload = Payload.builder()
                .review_id(dto.getId())
                .isbn(dto.getIsbn())
                .user_id(dto.getUserId())
                .content(dto.getContent())
                .build();

        KafkaBookReviewDto kafkaDto = new KafkaBookReviewDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(kafkaDto);
            log.info("[BookReviewProducer] send to {}: {}", topic, json);
            // 실제로 kafkaTemplate.send(topic, json) 하면 됩니다.
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return dto;
    }
}
