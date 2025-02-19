package com.example.communityservice.controller;

import com.example.communityservice.dto.PostDto;
import com.example.communityservice.service.PostService;
import com.example.communityservice.vo.RequestPost;
import com.example.communityservice.vo.ResponsePost;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community-service/posts")
@Slf4j
public class PostController {

    private final PostService postService;
    private final ModelMapper mapper = new ModelMapper();

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 생성
    @PostMapping
    public ResponsePost createPost(@RequestBody RequestPost req) {
        PostDto postDto = mapper.map(req, PostDto.class);
        PostDto created = postService.createPost(postDto);
        return mapper.map(created, ResponsePost.class);
    }

    // 게시글 단건 조회
    @GetMapping("/{postId}")
    public ResponsePost getPost(@PathVariable Long postId) {
        PostDto dto = postService.getPost(postId);
        if (dto == null) return null;
        return mapper.map(dto, ResponsePost.class);
    }

    // 특정 유저의 게시글 목록 조회
    @GetMapping("/user/{userId}")
    public List<ResponsePost> getPostsByUserId(@PathVariable String userId) {
        List<PostDto> postDtos = postService.getPostsByUserId(userId);
        return postDtos.stream().map(p -> mapper.map(p, ResponsePost.class)).toList();
    }

    // 전체 게시글 목록
    @GetMapping
    public List<ResponsePost> getAllPosts() {
        List<PostDto> postDtos = postService.getAllPosts();
        return postDtos.stream().map(p -> mapper.map(p, ResponsePost.class)).toList();
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponsePost updatePost(@PathVariable Long postId, @RequestBody RequestPost req) {
        PostDto dto = mapper.map(req, PostDto.class);
        PostDto updated = postService.updatePost(postId, dto);
        if (updated == null) return null;
        return mapper.map(updated, ResponsePost.class);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public boolean deletePost(@PathVariable Long postId) {
        return postService.deletePost(postId);
    }
}
