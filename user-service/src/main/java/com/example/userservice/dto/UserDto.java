package com.example.userservice.dto;

import com.example.userservice.vo.ResponseReview;
import com.example.userservice.vo.ResponsePost;
import com.example.userservice.vo.ResponseComment;
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

    // 기존에 있던 리뷰 목록
    private List<ResponseReview> reviews;

    // 새로 추가: community-service에서 가져온 게시글 목록
    private List<ResponsePost> posts;

    // 새로 추가: community-service에서 가져온 댓글 목록
    private List<ResponseComment> comments;
}
