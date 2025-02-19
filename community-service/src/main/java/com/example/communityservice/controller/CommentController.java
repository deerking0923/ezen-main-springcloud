package com.example.communityservice.controller;

import com.example.communityservice.dto.CommentDto;
import com.example.communityservice.service.CommentService;
import com.example.communityservice.vo.RequestComment;
import com.example.communityservice.vo.ResponseComment;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community-service/comments")
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final ModelMapper mapper = new ModelMapper();

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성
    @PostMapping
    public ResponseComment createComment(@RequestBody RequestComment req) {
        CommentDto dto = mapper.map(req, CommentDto.class);
        CommentDto created = commentService.createComment(dto);
        if (created == null) return null;  // postId가 유효하지 않을 경우 등
        return mapper.map(created, ResponseComment.class);
    }

    // 댓글 단건 조회
    @GetMapping("/{commentId}")
    public ResponseComment getComment(@PathVariable Long commentId) {
        CommentDto dto = commentService.getComment(commentId);
        if (dto == null) return null;
        return mapper.map(dto, ResponseComment.class);
    }

    // 특정 유저가 작성한 댓글 목록
    @GetMapping("/user/{userId}")
    public List<ResponseComment> getCommentsByUserId(@PathVariable String userId) {
        List<CommentDto> dtos = commentService.getCommentsByUserId(userId);
        return dtos.stream().map(c -> mapper.map(c, ResponseComment.class)).toList();
    }

    // 전체 댓글 목록
    @GetMapping
    public List<ResponseComment> getAllComments() {
        List<CommentDto> dtos = commentService.getAllComments();
        return dtos.stream().map(c -> mapper.map(c, ResponseComment.class)).toList();
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseComment updateComment(@PathVariable Long commentId, @RequestBody RequestComment req) {
        CommentDto dto = mapper.map(req, CommentDto.class);
        CommentDto updated = commentService.updateComment(commentId, dto);
        if (updated == null) return null;
        return mapper.map(updated, ResponseComment.class);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public boolean deleteComment(@PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }
}
