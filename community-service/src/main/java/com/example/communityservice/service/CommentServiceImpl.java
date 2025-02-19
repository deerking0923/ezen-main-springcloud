package com.example.communityservice.service;

import com.example.communityservice.dto.CommentDto;
import com.example.communityservice.jpa.CommentEntity;
import com.example.communityservice.jpa.CommentRepository;
import com.example.communityservice.jpa.PostEntity;
import com.example.communityservice.jpa.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper mapper = new ModelMapper();

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(CommentDto dto) {
        // postId 유효성 체크
        Optional<PostEntity> postOpt = postRepository.findById(dto.getPostId());
        if (postOpt.isEmpty()) return null;

        // 엔티티 매핑
        CommentEntity entity = mapper.map(dto, CommentEntity.class);
        entity.setPost(postOpt.get());   // ManyToOne 관계 설정
        commentRepository.save(entity);

        return mapper.map(entity, CommentDto.class);
    }

    @Override
    public CommentDto getComment(Long commentId) {
        Optional<CommentEntity> commentOpt = commentRepository.findById(commentId);
        return commentOpt.map(c -> mapper.map(c, CommentDto.class)).orElse(null);
    }

    @Override
    public List<CommentDto> getAllComments() {
        List<CommentEntity> comments = (List<CommentEntity>) commentRepository.findAll();
        return comments.stream().map(c -> mapper.map(c, CommentDto.class)).toList();
    }

    @Override
    public List<CommentDto> getCommentsByUserId(String userId) {
        List<CommentEntity> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                .map(c -> {
                    CommentDto commentDto = mapper.map(c, CommentDto.class);
                    // postId만 따로 매핑
                    commentDto.setPostId(c.getPost().getId());
                    return commentDto;
                })
                .toList();
    }

    @Override
    public CommentDto updateComment(Long commentId, CommentDto dto) {
        Optional<CommentEntity> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) return null;

        CommentEntity entity = commentOpt.get();
        entity.setContent(dto.getContent()); // 수정
        commentRepository.save(entity);

        return mapper.map(entity, CommentDto.class);
    }

    @Override
    public boolean deleteComment(Long commentId) {
        Optional<CommentEntity> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) return false;

        commentRepository.delete(commentOpt.get());
        return true;
    }
}
