// src/main/java/com/example/booksearchservice/dto/Payload.java
package com.example.bookreviewservice.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {
    private Long review_id;
    private String isbn;
    private String user_id;
    private String content;
    private LocalDate createDate;
}
