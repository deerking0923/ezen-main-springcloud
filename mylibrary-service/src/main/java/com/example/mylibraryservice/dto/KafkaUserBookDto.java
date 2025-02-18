package com.example.mylibraryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaUserBookDto {
    private Schema schema;
    private Payload payload;
}
