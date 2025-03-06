package com.example.communityservice.service;

import com.example.communityservice.client.UserServiceClient;
import com.example.communityservice.dto.PostDto;
import com.example.communityservice.jpa.PostEntity;
import com.example.communityservice.jpa.PostRepository;
import com.example.communityservice.vo.ResponseComment;
import com.example.communityservice.vo.ResponsePost;
import com.example.communityservice.vo.ResponseUser;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper mapper = new ModelMapper();
    private final UserServiceClient userServiceClient;

    public PostServiceImpl(PostRepository postRepository, UserServiceClient userServiceClient) {
        this.postRepository = postRepository;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public PostDto createPost(PostDto dto) {
        try {
            ResponseUser user = userServiceClient.getUser(dto.getUserId());
            if (user != null) {
                dto.setAuthor(user.getName());
            } else {
                dto.setAuthor("Unknown");
            }
        } catch (Exception e) {
            log.error("Failed to retrieve user info", e);
            dto.setAuthor("Unknown");
        }
        PostEntity entity = mapper.map(dto, PostEntity.class);
        entity = postRepository.save(entity);
        return mapper.map(entity, PostDto.class);
    }

    @Override
    public PostDto getPost(Long postId) {
        Optional<PostEntity> optional = postRepository.findById(postId);
        if (optional.isEmpty()) return null;
        PostEntity postEntity = optional.get();
    
        // ModelMapper나 수동 매핑을 통해 PostEntity를 PostDto로 변환
        ModelMapper mapper = new ModelMapper();
        PostDto postDto = mapper.map(postEntity, PostDto.class);
        return postDto;
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
