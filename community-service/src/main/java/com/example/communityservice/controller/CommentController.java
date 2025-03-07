package com.example.communityservice.controller;

import com.example.communityservice.dto.CommentDto;
import com.example.communityservice.service.CommentService;
import com.example.communityservice.vo.RequestComment;
import com.example.communityservice.vo.ResponseComment;
import com.example.communityservice.vo.ResponseUser;
import com.example.communityservice.client.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/community-service/comments")
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final ModelMapper mapper;
    private final UserServiceClient userServiceClient;

    public CommentController(CommentService commentService, UserServiceClient userServiceClient) {
        this.commentService = commentService;
        this.userServiceClient = userServiceClient;
        this.mapper = new ModelMapper();
    }

    // READ: 전체 댓글 조회 (모두 접근 가능)
    @GetMapping
    public List<ResponseComment> getAllComments() {
        List<CommentDto> dtos = commentService.getAllComments();
        return dtos.stream()
                .map(dto -> mapper.map(dto, ResponseComment.class))
                .collect(Collectors.toList());
    }

    // READ: 댓글 단건 조회 (모두 접근 가능)
    @GetMapping("/{commentId}")
    public ResponseComment getComment(@PathVariable Long commentId) {
        CommentDto dto = commentService.getComment(commentId);
        if (dto == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.");
        return mapper.map(dto, ResponseComment.class);
    }

    // READ: 특정 유저가 작성한 댓글 조회 (모두 접근 가능)
    @GetMapping("/user/{userId}")
    public List<ResponseComment> getCommentsByUserId(@PathVariable String userId) {
        List<CommentDto> dtos = commentService.getCommentsByUserId(userId);
        return dtos.stream()
                .map(dto -> mapper.map(dto, ResponseComment.class))
                .collect(Collectors.toList());
    }

    // READ: 특정 게시글의 댓글 조회 (모두 접근 가능)
    @GetMapping("/post/{postId}")
    public List<ResponseComment> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentDto> dtos = commentService.getCommentsByPostId(postId);
        return dtos.stream()
                .map(dto -> mapper.map(dto, ResponseComment.class))
                .collect(Collectors.toList());
    }

    // WRITE: 댓글 생성 (로그인 사용자만)
    @PostMapping("/{userId}")
    public ResponseComment createComment(@PathVariable String userId, @RequestBody RequestComment req) {
        if (!isOwner(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 작성 권한이 없습니다.");
        
        // user-service에서 username을 한 번만 조회
        ResponseUser user = userServiceClient.getUser(userId);
        String userName = user != null ? user.getName() : userId;

        ModelMapper localMapper = new ModelMapper();
        localMapper.getConfiguration().setAmbiguityIgnored(true);
        Converter<RequestComment, CommentDto> converter = new Converter<RequestComment, CommentDto>() {
            @Override
            public CommentDto convert(MappingContext<RequestComment, CommentDto> context) {
                RequestComment source = context.getSource();
                CommentDto dest = new CommentDto();
                dest.setContent(source.getContent());
                dest.setPostId(source.getPostId());
                return dest;
            }
        };
        localMapper.addConverter(converter);
        
        CommentDto dto = localMapper.map(req, CommentDto.class);
        dto.setUserId(userId);
        dto.setUserName(userName);
        
        CommentDto created = commentService.createComment(dto);
        if (created == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 생성 실패");
        return localMapper.map(created, ResponseComment.class);
    }

    // WRITE: 댓글 수정 (로그인 사용자만)
    @PutMapping("/{userId}/{commentId}")
    public ResponseComment updateComment(@PathVariable String userId, @PathVariable Long commentId,
                                           @RequestBody RequestComment req) {
        if (!isOwner(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 수정 권한이 없습니다.");
        CommentDto dto = mapper.map(req, CommentDto.class);
        dto.setUserId(userId);
        CommentDto updated = commentService.updateComment(commentId, dto);
        if (updated == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없거나 권한이 없습니다.");
        return mapper.map(updated, ResponseComment.class);
    }

    // WRITE: 댓글 삭제 (로그인 사용자만)
    @DeleteMapping("/{userId}/{commentId}")
    public boolean deleteComment(@PathVariable String userId, @PathVariable Long commentId) {
        if (!isOwner(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 삭제 권한이 없습니다.");
        boolean result = commentService.deleteComment(commentId);
        if (!result)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.");
        return true;
    }

    // 임시 보안 체크 메서드
    private boolean isOwner(String userIdFromPath) {
        return true;
    }
}
