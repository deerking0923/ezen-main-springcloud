package com.example.communityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaCommentDto {
    private Schema schema;
    private Payload payload;
}
