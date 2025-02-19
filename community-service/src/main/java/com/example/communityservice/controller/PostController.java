package com.example.communityservice.controller;

import com.example.communityservice.dto.PostDto;
import com.example.communityservice.service.PostService;
import com.example.communityservice.vo.RequestPost;
import com.example.communityservice.vo.ResponsePost;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community-service/posts")
@Slf4j
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponsePost createPost(@RequestBody RequestPost requestPost) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        PostDto postDto = mapper.map(requestPost, PostDto.class);
        PostDto created = postService.createPost(postDto);
        return mapper.map(created, ResponsePost.class);
    }

    @GetMapping("/{postId}")
    public ResponsePost getPost(@PathVariable("postId") Long postId) {
        PostDto dto = postService.getPost(postId);
        if (dto == null) return null;
        return new ModelMapper().map(dto, ResponsePost.class);
    }

    @GetMapping
    public List<ResponsePost> getAllPosts() {
        List<PostDto> postList = postService.getAllPosts();
        ModelMapper mapper = new ModelMapper();
        return postList.stream()
                .map(p -> mapper.map(p, ResponsePost.class))
                .toList();
    }

    @PutMapping("/{postId}")
    public ResponsePost updatePost(@PathVariable("postId") Long postId,
                                   @RequestBody RequestPost requestPost) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        PostDto postDto = mapper.map(requestPost, PostDto.class);
        PostDto updated = postService.updatePost(postId, postDto);
        if (updated == null) return null;
        return mapper.map(updated, ResponsePost.class);
    }

    @DeleteMapping("/{postId}")
    public boolean deletePost(@PathVariable("postId") Long postId) {
        return postService.deletePost(postId);
    }
}
