package com.example.recentreviewservice.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 받는 생성자 추가 (선택)
public class Payload {
    private Long id;         // JSON의 "id"와 일치
    private String isbn;
    private String userId;     // JSON의 "userId"와 일치
    private String content;
    private LocalDate createDate;
}
