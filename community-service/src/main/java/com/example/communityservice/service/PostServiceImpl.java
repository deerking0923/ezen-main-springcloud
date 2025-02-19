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
    public PostDto createPost(PostDto dto) {
        PostEntity entity = mapper.map(dto, PostEntity.class);
        postRepository.save(entity);
        return mapper.map(entity, PostDto.class);
    }

    @Override
    public PostDto getPost(Long postId) {
        Optional<PostEntity> optional = postRepository.findById(postId);
        return optional.map(post -> mapper.map(post, PostDto.class)).orElse(null);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<PostEntity> posts = (List<PostEntity>) postRepository.findAll();
        return posts.stream().map(p -> mapper.map(p, PostDto.class)).toList();
    }

    @Override
    public List<PostDto> getPostsByUserId(String userId) {
        List<PostEntity> posts = postRepository.findByUserId(userId);
        return posts.stream().map(p -> mapper.map(p, PostDto.class)).toList();
    }

    @Override
    public PostDto updatePost(Long postId, PostDto dto) {
        Optional<PostEntity> optional = postRepository.findById(postId);
        if (optional.isEmpty()) return null;

        PostEntity entity = optional.get();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        postRepository.save(entity);

        return mapper.map(entity, PostDto.class);
    }

    @Override
    public boolean deletePost(Long postId) {
        Optional<PostEntity> optional = postRepository.findById(postId);
        if (optional.isEmpty()) return false;

        postRepository.delete(optional.get());
        return true;
    }
}
