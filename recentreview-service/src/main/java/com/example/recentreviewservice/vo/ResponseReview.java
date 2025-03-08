package com.example.recentreviewservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseReview {
    private String isbn;
    private String userId;
    private String content;
    private LocalDate createDate;
}
