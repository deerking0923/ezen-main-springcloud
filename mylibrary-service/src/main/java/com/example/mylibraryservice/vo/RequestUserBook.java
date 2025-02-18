package com.example.mylibraryservice.vo;

import com.example.mylibraryservice.dto.BookQuoteDto;
import lombok.Data;
import java.util.List;

@Data
public class RequestUserBook {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String pDate;
    private String description;
    private String thumbnail;
    private String personalReview;
    private List<BookQuoteDto> quotes;
    private String startDate;
    private String endDate;
}
