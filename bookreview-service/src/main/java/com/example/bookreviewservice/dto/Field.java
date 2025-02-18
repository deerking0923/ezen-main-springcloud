// src/main/java/com/example/booksearchservice/dto/Field.java
package com.example.bookreviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Field {
    private String type;
    private boolean optional;
    private String field;
}
