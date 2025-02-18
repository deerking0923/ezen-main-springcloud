// src/main/java/com/example/booksearchservice/vo/RequestReview.java
package com.example.bookreviewservice.vo;

import lombok.Data;

@Data
public class RequestReview {
    private String isbn;
    private String content;
}
