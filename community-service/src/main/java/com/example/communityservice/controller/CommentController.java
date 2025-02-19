package com.example.communityservice.controller;

import com.example.communityservice.dto.CommentDto;
import com.example.communityservice.service.CommentService;
import com.example.communityservice.vo.RequestComment;
import com.example.communityservice.vo.ResponseComment;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community-service")
@Slf4j
public class CommentController {

    private final Environment env;
    private final CommentService commentService;

    public CommentController(Environment env, CommentService commentService) {
        this.env = env;
        this.commentService = commentService;
    }

    @GetMapping("/health-check")
    public String status() {
        return String.format("Community Service (Comment) is working on local port %s, server port %s",
                env.getProperty("local.server.port"),
                env.getProperty("server.port"));
    }

    /**
     * 1) 댓글 생성
     * POST /community-service/{userId}/comments
     */
    @PostMapping("/{userId}/comments")
    public ResponseEntity<ResponseComment> createComment(@PathVariable("userId") Long userId,
                                                         @RequestBody RequestComment commentDetails) {
        log.info("Creating comment for userId: {}", userId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        CommentDto commentDto = new CommentDto();
        commentDto.setPostId(commentDetails.getPostId());
        commentDto.setContent(commentDetails.getContent());
        commentDto.setUserId(userId);

        CommentDto created = commentService.createComment(commentDto);
        ResponseComment responseComment = mapper.map(created, ResponseComment.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseComment);
    }

    /**
     * 2) 댓글 수정
     * PUT /community-service/{userId}/comments/{commentId}
     */
    @PutMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<ResponseComment> updateComment(@PathVariable("userId") Long userId,
                                                         @PathVariable("commentId") Long commentId,
                                                         @RequestBody RequestComment commentDetails) {
        log.info("Updating comment {} for userId: {}", commentId, userId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        CommentDto commentDto = new CommentDto();
        commentDto.setContent(commentDetails.getContent());
        commentDto.setUserId(userId);

        CommentDto updated = commentService.updateComment(commentId, commentDto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        ResponseComment responseComment = mapper.map(updated, ResponseComment.class);
        return ResponseEntity.ok(responseComment);
    }

    /**
     * 3) 댓글 삭제
     * DELETE /community-service/{userId}/comments/{commentId}
     */
    @DeleteMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("userId") Long userId,
                                           @PathVariable("commentId") Long commentId) {
        log.info("Deleting comment {} for userId: {}", commentId, userId);
        boolean deleted = commentService.deleteComment(commentId, userId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 4) 특정 postId의 댓글 목록 조회
     * GET /community-service/comments/post/{postId}
     */
    @GetMapping("/comments/post/{postId}")
    public ResponseEntity<List<ResponseComment>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        ModelMapper mapper = new ModelMapper();
        List<ResponseComment> result = comments.stream()
                .map(c -> mapper.map(c, ResponseComment.class))
                .toList();
        return ResponseEntity.ok(result);
    }

    /**
     * 5) 특정 댓글 단건 조회
     * GET /community-service/comments/{commentId}
     */
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<ResponseComment> getComment(@PathVariable("commentId") Long commentId) {
        CommentDto commentDto = commentService.getComment(commentId);
        if (commentDto == null) {
            return ResponseEntity.notFound().build();
        }
        ModelMapper mapper = new ModelMapper();
        ResponseComment res = mapper.map(commentDto, ResponseComment.class);
        return ResponseEntity.ok(res);
    }
}
