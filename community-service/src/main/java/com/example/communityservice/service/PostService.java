package com.example.communityservice.service;

import com.example.communityservice.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostDto getPost(Long postId);
    List<PostDto> getAllPosts();
    PostDto updatePost(Long postId, PostDto postDto);
    boolean deletePost(Long postId);
}
