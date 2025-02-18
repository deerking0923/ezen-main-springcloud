package com.example.mylibraryservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserBookDto {
    private Long id;
    private String userId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String pDate;
    private String description;
    private String thumbnail;
    private String personalReview;
    // 기존에 List<BookQuoteEntity>로 되어 있다면 아래처럼 BookQuoteDto로 변경
    private List<BookQuoteDto> quotes;
    private String startDate;
    private String endDate;
}
