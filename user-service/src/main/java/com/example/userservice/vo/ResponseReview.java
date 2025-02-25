package com.example.userservice.vo;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ResponseReview {
    private Long id;
    private String isbn;
    private String userId;
    private LocalDate createDate;
    private String content;
}
