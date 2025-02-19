package com.example.communityservice.service;

import com.example.communityservice.dto.CommentDto;
import com.example.communityservice.jpa.CommentEntity;
import com.example.communityservice.jpa.CommentRepository;
import com.example.communityservice.messagequeue.CommentProducer;
import com.example.communityservice.messagequeue.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final KafkaProducer kafkaProducer;       // 단순 직렬화 Producer
    private final CommentProducer commentProducer;   // Struct 형식 Producer

    public CommentServiceImpl(CommentRepository commentRepository,
                              KafkaProducer kafkaProducer,
                              CommentProducer commentProducer) {
        this.commentRepository = commentRepository;
        this.kafkaProducer = kafkaProducer;
        this.commentProducer = commentProducer;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        // Entity 매핑
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        CommentEntity entity = mapper.map(commentDto, CommentEntity.class);
        commentRepository.save(entity);

        // DB 저장 후 다시 DTO로 매핑
        CommentDto created = mapper.map(entity, CommentDto.class);

        // Kafka 발행
        kafkaProducer.send("comment-topic", created);
        commentProducer.send("comment-topic-struct", created);

        return created;
    }

    @Override
    public CommentDto updateComment(Long commentId, CommentDto commentDto) {
        Optional<CommentEntity> optionalEntity = commentRepository.findById(commentId);
        if (optionalEntity.isEmpty()) {
            return null;
        }
        CommentEntity entity = optionalEntity.get();

        // 작성자( userId ) 체크 로직 필요 시 추가
        entity.setContent(commentDto.getContent());
        commentRepository.save(entity);

        // 수정된 엔티티 -> DTO
        ModelMapper mapper = new ModelMapper();
        CommentDto updated = mapper.map(entity, CommentDto.class);

        // Kafka 발행 (수정 이벤트)
        kafkaProducer.send("comment-topic", updated);
        commentProducer.send("comment-topic-struct-update", updated);

        return updated;
    }

    @Override
    public boolean deleteComment(Long commentId, Long userId) {
        Optional<CommentEntity> optionalEntity = commentRepository.findById(commentId);
        if (optionalEntity.isEmpty()) {
            return false;
        }
        CommentEntity entity = optionalEntity.get();

        // userId 체크
        if (!entity.getUserId().equals(userId)) {
            return false; // 권한 없음
        }

        commentRepository.delete(entity);

        // Kafka 발행 (삭제 이벤트)
        CommentDto dto = new CommentDto();
        dto.setId(commentId);
        dto.setUserId(userId);

        kafkaProducer.send("comment-topic-delete", dto);
        commentProducer.send("comment-topic-struct-delete", dto);

        return true;
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        ModelMapper mapper = new ModelMapper();
        List<CommentEntity> entities = commentRepository.findByPostId(postId);
        return entities.stream()
                .map(e -> mapper.map(e, CommentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getComment(Long commentId) {
        Optional<CommentEntity> optionalEntity = commentRepository.findById(commentId);
        if (optionalEntity.isEmpty()) {
            return null;
        }
        return new ModelMapper().map(optionalEntity.get(), CommentDto.class);
    }
}
