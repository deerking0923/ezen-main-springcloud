// src/main/java/com/example/booksearchservice/dto/Payload.java
package com.example.bookreviewservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {
    private Long review_id;
    private String isbn;
    private Long user_id;
    private String content;
}
