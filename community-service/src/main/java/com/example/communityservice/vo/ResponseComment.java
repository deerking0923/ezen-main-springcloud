package com.example.communityservice.vo;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ResponseComment {
    private Long id;
    private String userId;
    private Long postId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;
    private String content;
    private String userName; // 댓글 작성자의 이름
}
