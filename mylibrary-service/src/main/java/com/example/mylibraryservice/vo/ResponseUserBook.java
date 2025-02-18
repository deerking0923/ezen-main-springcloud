package com.example.mylibraryservice.vo;

import lombok.Data;
import java.util.List;

import com.example.mylibraryservice.dto.BookQuoteDto;

@Data
public class ResponseUserBook {
    private Long id;         // DB pk
    private String userId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String pDate;
    private String description;
    private String thumbnail;
    private String personalReview;
    // 기존에는 quotes가 string이었지만, 이제 List<BookQuoteDto> 형태로 관리
    private List<BookQuoteDto> quotes;
    private String startDate;
    private String endDate;
}
