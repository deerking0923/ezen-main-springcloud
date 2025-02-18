// src/main/java/com/example/mylibraryservice/messagequeue/MyLibraryProducer.java
package com.example.mylibraryservice.messagequeue;

import com.example.mylibraryservice.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class MyLibraryProducer {

    public UserBookDto send(String topic, UserBookDto userBookDto) {
        // Kafka에 전송할 DTO 구조를 구성
        // (Schema, Payload 활용) -> order-service의 OrderProducer와 같은 로직
        List<Field> fields = Arrays.asList(
                new Field("string", true, "user_id"),
                new Field("string", true, "isbn"),
                new Field("string", true, "title"),
                new Field("string", true, "author")
        );

        Schema schema = Schema.builder()
                .type("struct")
                .fields(fields)
                .optional(false)
                .name("user_books")
                .build();

        Payload payload = Payload.builder()
                .user_id(userBookDto.getUserId())
                .isbn(userBookDto.getIsbn())
                .title(userBookDto.getTitle())
                .author(userBookDto.getAuthor())
                .build();

        KafkaUserBookDto kafkaUserBookDto = new KafkaUserBookDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaUserBookDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        log.info("[MyLibraryProducer] Sent data to Kafka: {}", jsonInString);

        // 실제 KafkaTemplate로 메시지 전송 로직 (프로젝트 상황에 맞게 수정)
        return userBookDto;
    }
}
