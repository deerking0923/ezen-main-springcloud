package com.example.communityservice.controller;

import com.example.communityservice.dto.PostDto;
import com.example.communityservice.service.PostService;
import com.example.communityservice.vo.RequestPost;
import com.example.communityservice.vo.ResponsePost;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/community-service/posts")
@Slf4j
public class PostController {

    private final PostService postService;
    private final ModelMapper mapper = new ModelMapper();

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // READ: 전체 게시글 조회 (모두 접근 가능)
    @GetMapping
    public List<ResponsePost> getAllPosts() {
        List<PostDto> dtos = postService.getAllPosts();
        return dtos.stream().map(dto -> mapper.map(dto, ResponsePost.class)).collect(Collectors.toList());
    }

    // READ: 게시글 단건 조회 (모두 접근 가능)
    @GetMapping("/{postId}")
    public ResponsePost getPost(@PathVariable Long postId) {
        PostDto dto = postService.getPost(postId);
        if (dto == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");
        return mapper.map(dto, ResponsePost.class);
    }

    // READ: 특정 유저의 게시글 조회 (모두 접근 가능)
    @GetMapping("/user/{userId}")
    public List<ResponsePost> getPostsByUserId(@PathVariable String userId) {
        List<PostDto> dtos = postService.getPostsByUserId(userId);
        return dtos.stream().map(dto -> mapper.map(dto, ResponsePost.class)).collect(Collectors.toList());
    }

    // WRITE: 게시글 생성 (로그인 사용자만)
    // POST /community-service/posts/{userId}
    @PostMapping("/{userId}")
    public ResponsePost createPost(@PathVariable String userId, @RequestBody RequestPost req) {
        if (!isOwner(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글 작성 권한이 없습니다.");
        PostDto dto = mapper.map(req, PostDto.class);
        dto.setUserId(userId);
        PostDto created = postService.createPost(dto);
        return mapper.map(created, ResponsePost.class);
    }

    // WRITE: 게시글 수정 (로그인 사용자만)
    // PUT /community-service/posts/{userId}/{postId}
    @PutMapping("/{userId}/{postId}")
    public ResponsePost updatePost(@PathVariable String userId, @PathVariable Long postId,
            @RequestBody RequestPost req) {
        if (!isOwner(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다.");
        PostDto dto = mapper.map(req, PostDto.class);
        dto.setUserId(userId);
        PostDto updated = postService.updatePost(postId, dto);
        if (updated == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없거나 권한이 없습니다.");
        return mapper.map(updated, ResponsePost.class);
    }

    // WRITE: 게시글 삭제 (로그인 사용자만)
    // DELETE /community-service/posts/{userId}/{postId}
    @DeleteMapping("/{userId}/{postId}")
    public boolean deletePost(@PathVariable String userId, @PathVariable Long postId) {
        if (!isOwner(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다.");
        boolean result = postService.deletePost(postId);
        if (!result)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");
        return true;
    }

    // 임시 보안 체크 메서드
    private boolean isOwner(String userIdFromPath) {
        return true;
    }
}
