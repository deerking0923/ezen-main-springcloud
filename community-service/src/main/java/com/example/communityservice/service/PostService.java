package com.example.communityservice.service;

import com.example.communityservice.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto dto);
    PostDto getPost(Long postId);
    List<PostDto> getAllPosts();
    List<PostDto> getPostsByUserId(String userId);
    PostDto updatePost(Long postId, PostDto dto);
    boolean deletePost(Long postId);
}
