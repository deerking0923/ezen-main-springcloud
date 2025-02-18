// src/main/java/com/example/booksearchservice/dto/KafkaBookReviewDto.java
package com.example.bookreviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaBookReviewDto {
    private Schema schema;
    private Payload payload;
}
