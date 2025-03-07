package com.example.userservice.service;

import com.example.userservice.client.BookReviewServiceClient;
import com.example.userservice.client.CommunityServiceClient; // 추가
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseReview;
import com.example.userservice.vo.ResponsePost;       // 추가
import com.example.userservice.vo.ResponseComment;   // 추가
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment env;
    private final RestTemplate restTemplate;
    private final BookReviewServiceClient bookReviewServiceClient;
    private final CommunityServiceClient communityServiceClient; // 추가
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           Environment env,
                           RestTemplate restTemplate,
                           BookReviewServiceClient bookReviewServiceClient,
                           CommunityServiceClient communityServiceClient, // 추가
                           CircuitBreakerFactory circuitBreakerFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.restTemplate = restTemplate;
        this.bookReviewServiceClient = bookReviewServiceClient;
        this.communityServiceClient = communityServiceClient; // 추가
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null)
            throw new UsernameNotFoundException(username + ": not found");

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null)
            throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        // 1) 리뷰 목록 가져오기
        log.info("Before calling bookreview-service to get reviews");
        List<ResponseReview> reviewList = new ArrayList<>();
        try {
            reviewList = bookReviewServiceClient.getReviews(userId);
        } catch (Exception ex) {
            log.error("Error fetching reviews: " + ex.getMessage());
        }
        userDto.setReviews(reviewList);
        log.info("After calling bookreview-service to get reviews");

        // 2) 커뮤니티 게시글/댓글 목록 가져오기 (community-service)
        //    Circuit Breaker를 사용하여 장애 대비
        log.info("Before calling community-service to get posts/comments");
        try {
            var circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

            List<ResponsePost> postList = circuitBreaker.run(
                () -> communityServiceClient.getPostsByUserId(userId),
                throwable -> new ArrayList<>() // 실패 시 빈 리스트
            );
            List<ResponseComment> commentList = circuitBreaker.run(
                () -> communityServiceClient.getCommentsByUserId(userId),
                throwable -> new ArrayList<>()
            );

            userDto.setPosts(postList);
            userDto.setComments(commentList);

        } catch (Exception ex) {
            log.error("Error fetching posts/comments: " + ex.getMessage());
            userDto.setPosts(new ArrayList<>());
            userDto.setComments(new ArrayList<>());
        }
        log.info("After calling community-service to get posts/comments");

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userEntity, UserDto.class);
        // 필요하다면 getUserByUserId 와 동일하게 리뷰, 게시글, 댓글 조회 로직 추가
        return userDto;
    }
}
