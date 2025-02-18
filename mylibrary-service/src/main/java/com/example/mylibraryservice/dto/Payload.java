package com.example.mylibraryservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {
    private String user_id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
}