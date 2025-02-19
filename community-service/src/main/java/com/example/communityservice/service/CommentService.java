package com.example.communityservice.service;

import com.example.communityservice.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto);
    CommentDto updateComment(Long commentId, CommentDto commentDto);
    boolean deleteComment(Long commentId, Long userId);
    List<CommentDto> getCommentsByPostId(Long postId);
    CommentDto getComment(Long commentId);
}
