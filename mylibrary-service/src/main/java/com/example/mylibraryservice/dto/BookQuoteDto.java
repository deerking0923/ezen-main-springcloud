package com.example.mylibraryservice.dto;

import lombok.Data;

@Data
public class BookQuoteDto {
    private Long id;
    private String pageNumber;
    private String quoteText;
}
