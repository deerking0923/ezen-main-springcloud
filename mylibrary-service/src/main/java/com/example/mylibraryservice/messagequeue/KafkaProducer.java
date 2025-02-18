// src/main/java/com/example/mylibraryservice/messagequeue/KafkaProducer.java
package com.example.mylibraryservice.messagequeue;

import com.example.mylibraryservice.dto.UserBookDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public UserBookDto send(String topic, UserBookDto userBookDto) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(userBookDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("[KafkaProducer] Sent data: " + userBookDto);

        return userBookDto;
    }
}
