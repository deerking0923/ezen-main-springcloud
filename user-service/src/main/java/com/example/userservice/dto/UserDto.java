package com.example.userservice.dto;

import com.example.userservice.vo.ResponseReview;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;
    private String decryptedPwd;
    private String encryptedPwd;
    
    // 기존의 주문 목록 orders 대신 리뷰 목록 reviews로 변경
    private List<ResponseReview> reviews;
}
