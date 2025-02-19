package com.example.communityservice.messagequeue;

import com.example.communityservice.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class CommentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CommentProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CommentDto send(String topic, CommentDto dto) {
        // schema, payload 구성
        List<Field> fields = Arrays.asList(
                new Field("int64", true, "comment_id"),
                new Field("int64", true, "post_id"),
                new Field("String", true, "user_id"),
                new Field("string", true, "content")
        );

        Schema schema = Schema.builder()
                .type("struct")
                .fields(fields)
                .optional(false)
                .name("comments")
                .build();

        Payload payload = Payload.builder()
                .comment_id(dto.getId())
                .post_id(dto.getPostId())
                .user_id(dto.getUserId())
                .content(dto.getContent())
                .build();

        KafkaCommentDto kafkaDto = new KafkaCommentDto(schema, payload);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            String json = mapper.writeValueAsString(kafkaDto);
            log.info("[CommentProducer] Sent to {}: {}", topic, json);
            kafkaTemplate.send(topic, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return dto;
    }
}
