package com.example.communityservice.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ResponsePost {
    private Long id;
    private String userId;
    private String author;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;
    private int viewCount;
    // private List<ResponseComment> comments;
}
