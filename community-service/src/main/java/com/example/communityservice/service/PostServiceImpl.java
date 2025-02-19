package com.example.communityservice.service;

import com.example.communityservice.dto.PostDto;
import com.example.communityservice.jpa.PostEntity;
import com.example.communityservice.jpa.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper mapper = new ModelMapper();

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        PostEntity entity = mapper.map(postDto, PostEntity.class);
        postRepository.save(entity);
        return mapper.map(entity, PostDto.class);
    }

    @Override
    public PostDto getPost(Long postId) {
        Optional<PostEntity> optionalEntity = postRepository.findById(postId);
        if (optionalEntity.isEmpty()) return null;
        return mapper.map(optionalEntity.get(), PostDto.class);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<PostEntity> entities = (List<PostEntity>) postRepository.findAll();
        return entities.stream()
                .map(e -> mapper.map(e, PostDto.class))
                .toList();
    }

    @Override
    public PostDto updatePost(Long postId, PostDto postDto) {
        Optional<PostEntity> optionalEntity = postRepository.findById(postId);
        if (optionalEntity.isEmpty()) {
            return null;
        }
        PostEntity entity = optionalEntity.get();
        entity.setTitle(postDto.getTitle());
        entity.setContent(postDto.getContent());
        postRepository.save(entity);
        return mapper.map(entity, PostDto.class);
    }

    @Override
    public boolean deletePost(Long postId) {
        Optional<PostEntity> optionalEntity = postRepository.findById(postId);
        if (optionalEntity.isEmpty()) return false;
        postRepository.delete(optionalEntity.get());
        return true;
    }
}
