package com.example.communityservice.service;

import com.example.communityservice.dto.CommentDto;
import com.example.communityservice.jpa.CommentEntity;
import com.example.communityservice.jpa.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper mapper = new ModelMapper();

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDto createComment(CommentDto dto) {
        CommentEntity entity = mapper.map(dto, CommentEntity.class);
        entity = commentRepository.save(entity);
        return mapper.map(entity, CommentDto.class);
    }

    @Override
    public CommentDto getComment(Long commentId) {
        Optional<CommentEntity> commentOpt = commentRepository.findById(commentId);
        return commentOpt.map(entity -> mapper.map(entity, CommentDto.class)).orElse(null);
    }

    @Override
    public List<CommentDto> getAllComments() {
        List<CommentEntity> entities = (List<CommentEntity>) commentRepository.findAll();
        return entities.stream()
                .map(entity -> mapper.map(entity, CommentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByUserId(String userId) {
        List<CommentEntity> entities = commentRepository.findByUserId(userId);
        return entities.stream()
                .map(entity -> mapper.map(entity, CommentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<CommentEntity> entities = commentRepository.findByPostId(postId);
        return entities.stream()
                .map(entity -> mapper.map(entity, CommentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto updateComment(Long commentId, CommentDto dto) {
        Optional<CommentEntity> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) return null;
        CommentEntity entity = commentOpt.get();
        entity.setContent(dto.getContent());
        entity = commentRepository.save(entity);
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
