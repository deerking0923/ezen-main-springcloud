package com.example.userservice.client;

import com.example.userservice.vo.ResponseComment;
import com.example.userservice.vo.ResponsePost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "community-service")
public interface CommunityServiceClient {

    // 특정 사용자(userId)가 작성한 게시글 목록 조회
    @GetMapping("/community-service/posts/user/{userId}")
    List<ResponsePost> getPostsByUserId(@PathVariable("userId") String userId);

    // 특정 사용자(userId)가 작성한 댓글 목록 조회
    @GetMapping("/community-service/comments/user/{userId}")
    List<ResponseComment> getCommentsByUserId(@PathVariable("userId") String userId);
}
