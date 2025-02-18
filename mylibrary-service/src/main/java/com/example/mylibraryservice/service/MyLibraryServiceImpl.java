// src/main/java/com/example/mylibraryservice/service/MyLibraryServiceImpl.java
package com.example.mylibraryservice.service;

import com.example.mylibraryservice.dto.UserBookDto;
import com.example.mylibraryservice.jpa.UserBookEntity;
import com.example.mylibraryservice.jpa.UserBookRepository;
import com.example.mylibraryservice.messagequeue.KafkaProducer;
import com.example.mylibraryservice.messagequeue.MyLibraryProducer;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MyLibraryServiceImpl implements MyLibraryService {

    private final UserBookRepository userBookRepository;
    private final KafkaProducer kafkaProducer;
    private final MyLibraryProducer myLibraryProducer;

    public MyLibraryServiceImpl(
            UserBookRepository userBookRepository,
            KafkaProducer kafkaProducer,
            MyLibraryProducer myLibraryProducer
    ) {
        this.userBookRepository = userBookRepository;
        this.kafkaProducer = kafkaProducer;
        this.myLibraryProducer = myLibraryProducer;
    }

    @Override
    public UserBookDto createUserBook(UserBookDto userBookDto) {
        // DB 저장
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserBookEntity userBookEntity = mapper.map(userBookDto, UserBookEntity.class);
        userBookRepository.save(userBookEntity);

        // Kafka 메시지 발행 (필요 시)
        kafkaProducer.send("example-library-topic", userBookDto);
        myLibraryProducer.send("example-library-topic-struct", userBookDto);

        return mapper.map(userBookEntity, UserBookDto.class);
    }

    @Override
    public List<UserBookDto> getUserBooks(String userId) {
        List<UserBookEntity> userBookEntities = userBookRepository.findByUserId(userId);
        List<UserBookDto> result = new ArrayList<>();

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        userBookEntities.forEach(entity -> {
            result.add(mapper.map(entity, UserBookDto.class));
        });

        return result;
    }
}
