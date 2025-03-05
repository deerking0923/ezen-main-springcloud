package com.example.communityservice.service;

import com.example.communityservice.dto.CommentDto;
import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto dto);
    CommentDto getComment(Long commentId);
    List<CommentDto> getAllComments();
    List<CommentDto> getCommentsByUserId(String userId);
    List<CommentDto> getCommentsByPostId(Long postId);  // 추가: 특정 게시글의 댓글 조회
    CommentDto updateComment(Long commentId, CommentDto dto);
    boolean deleteComment(Long commentId);
}
