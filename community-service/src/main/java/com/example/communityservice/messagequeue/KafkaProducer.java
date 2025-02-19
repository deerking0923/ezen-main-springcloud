package com.example.communityservice.messagequeue;

import com.example.communityservice.dto.CommentDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 예: 단순 JSON 직렬화용 Producer (CommentProducer와 병행해서 사용할 수 있음)
 */
@Service
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CommentDto send(String topic, CommentDto dto) {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(topic, jsonInString);
        log.info("[KafkaProducer] Sent data to {}: {}", topic, jsonInString);
        return dto;
    }
}
