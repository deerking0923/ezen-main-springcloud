package com.example.bookreviewservice.messagequeue;

import com.example.bookreviewservice.dto.BookReviewDto;
import com.example.bookreviewservice.dto.Field;
import com.example.bookreviewservice.dto.KafkaBookReviewDto;
import com.example.bookreviewservice.dto.Payload;
import com.example.bookreviewservice.dto.Schema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // 추가
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate; // 추가
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class BookReviewProducer {

    private final KafkaTemplate<String, String> kafkaTemplate; // 추가

    public BookReviewProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookReviewDto send(String topic, BookReviewDto dto) {
        List<Field> fields = Arrays.asList(
                new Field("int64", true, "review_id"),
                new Field("string", true, "isbn"),
                new Field("string", true, "user_id"),
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
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()); // 모듈 등록
        try {
            String json = mapper.writeValueAsString(kafkaDto);
            log.info("[BookReviewProducer] Sent to {}: {}", topic, json);
            kafkaTemplate.send(topic, json); // 실제 전송
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return dto;
    }
}
