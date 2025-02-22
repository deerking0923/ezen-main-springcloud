package com.example.mylibraryservice.service;

import com.example.mylibraryservice.dto.BookQuoteDto;
import com.example.mylibraryservice.dto.UserBookDto;
import com.example.mylibraryservice.jpa.BookQuoteEntity;
import com.example.mylibraryservice.jpa.BookQuoteRepository;
import com.example.mylibraryservice.jpa.UserBookEntity;
import com.example.mylibraryservice.jpa.UserBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MyLibraryServiceImpl implements MyLibraryService {

    private final UserBookRepository userBookRepository;
    private final BookQuoteRepository bookQuoteRepository;

    public MyLibraryServiceImpl(
            UserBookRepository userBookRepository,
            BookQuoteRepository bookQuoteRepository) {
        this.userBookRepository = userBookRepository;
        this.bookQuoteRepository = bookQuoteRepository;
    }

    @Override
    public UserBookDto createUserBook(UserBookDto userBookDto) {
        // 1) ModelMapper 설정
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 2) UserBookEntity를 만든다
        UserBookEntity userBookEntity = mapper.map(userBookDto, UserBookEntity.class);

        // 만약 quotes가 있다면, BookQuoteEntity를 생성하면서 userBook을 설정
        if (userBookDto.getQuotes() != null && !userBookDto.getQuotes().isEmpty()) {
            List<BookQuoteEntity> quoteEntities = new ArrayList<>();
            for (BookQuoteDto quoteDto : userBookDto.getQuotes()) {
                BookQuoteEntity quoteEntity = new BookQuoteEntity();
                quoteEntity.setPageNumber(quoteDto.getPageNumber());
                quoteEntity.setQuoteText(quoteDto.getQuoteText());
                // userBook 설정
                quoteEntity.setUserBook(userBookEntity);
                quoteEntities.add(quoteEntity);
            }
            // userBookEntity에 연결 (CascadeType.ALL로 자식 자동 저장)
            userBookEntity.setQuotes(quoteEntities);
        }

        // 3) DB 저장
        userBookRepository.save(userBookEntity);

        // 5) 저장된 UserBookEntity를 UserBookDto로 변환하여 반환
        return mapper.map(userBookEntity, UserBookDto.class);
    }

    @Override
    public List<UserBookDto> getUserBooks(String userId) {
        List<UserBookEntity> entities = userBookRepository.findByUserId(userId);
        List<UserBookDto> result = new ArrayList<>();

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        for (UserBookEntity entity : entities) {
            // 1) UserBookEntity -> UserBookDto 매핑
            UserBookDto dto = mapper.map(entity, UserBookDto.class);

            // 2) BookQuoteEntity 목록 조회 (자식들)
            List<BookQuoteEntity> quoteEntities = bookQuoteRepository.findByUserBook_Id(entity.getId());
            List<BookQuoteDto> quoteDtos = new ArrayList<>();

            // 3) BookQuoteEntity -> BookQuoteDto 매핑
            for (BookQuoteEntity qe : quoteEntities) {
                BookQuoteDto quoteDto = new BookQuoteDto();
                quoteDto.setId(qe.getId());
                quoteDto.setPageNumber(qe.getPageNumber());
                quoteDto.setQuoteText(qe.getQuoteText());
                quoteDtos.add(quoteDto);
            }

            // 4) UserBookDto에 quotes 리스트로 설정
            dto.setQuotes(quoteDtos);

            result.add(dto);
        }

        return result;
    }

    @Override
    public UserBookDto getUserBook(String userId, Long bookId) {
        // 1) userId와 bookId에 맞는 userBook을 찾는다.
        UserBookEntity userBookEntity = userBookRepository.findById(bookId)
                .orElse(null);
        // 예외 케이스: 책이 존재하지 않거나 userId가 다르면 null
        if (userBookEntity == null || !userBookEntity.getUserId().equals(userId)) {
            return null;
        }

        // 2) DTO 변환
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserBookDto userBookDto = mapper.map(userBookEntity, UserBookDto.class);

        // 3) 기록 문장(book quotes)도 함께 조회
        List<BookQuoteEntity> quoteEntities = bookQuoteRepository.findByUserBook_Id(bookId);
        List<BookQuoteDto> quoteDtos = new ArrayList<>();
        for (BookQuoteEntity qe : quoteEntities) {
            BookQuoteDto quoteDto = new BookQuoteDto();
            quoteDto.setId(qe.getId());
            quoteDto.setPageNumber(qe.getPageNumber());
            quoteDto.setQuoteText(qe.getQuoteText());
            quoteDtos.add(quoteDto);
        }
        userBookDto.setQuotes(quoteDtos);

        // 4) 최종 DTO 반환
        return userBookDto;
    }

    @Override
    public boolean deleteUserBook(String userId, Long bookId) {
        Optional<UserBookEntity> optionalEntity = userBookRepository.findById(bookId);
        if (optionalEntity.isEmpty() || !optionalEntity.get().getUserId().equals(userId)) {
            return false;
        }
        try {
            userBookRepository.delete(optionalEntity.get());
            return true;
        } catch (Exception e) {
            log.error("Error deleting user book", e);
            return false;
        }
    }

    @Override
    public UserBookDto updateUserBook(UserBookDto userBookDto) {
        // 1. 해당 책 기록이 존재하는지 확인
        Optional<UserBookEntity> optionalEntity = userBookRepository.findById(userBookDto.getId());
        if (optionalEntity.isEmpty() || !optionalEntity.get().getUserId().equals(userBookDto.getUserId())) {
            return null;
        }
        UserBookEntity existingEntity = optionalEntity.get();

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 2. 기존 엔티티의 필드 업데이트
        existingEntity.setIsbn(userBookDto.getIsbn());
        existingEntity.setTitle(userBookDto.getTitle());
        existingEntity.setAuthor(userBookDto.getAuthor());
        existingEntity.setPublisher(userBookDto.getPublisher());
        existingEntity.setPDate(userBookDto.getPDate());
        existingEntity.setDescription(userBookDto.getDescription());
        existingEntity.setThumbnail(userBookDto.getThumbnail());
        existingEntity.setPersonalReview(userBookDto.getPersonalReview());
        existingEntity.setStartDate(userBookDto.getStartDate());
        existingEntity.setEndDate(userBookDto.getEndDate());

        // 3. 자식(기록 문장) 업데이트
        // 기존 컬렉션을 clear()하고 새 항목들을 추가 (양방향 관계 유지)
        if (userBookDto.getQuotes() != null) {
            // 컬렉션이 null이 아니면 클리어, 아니면 새 리스트 생성
            if (existingEntity.getQuotes() != null) {
                existingEntity.getQuotes().clear();
            } else {
                existingEntity.setQuotes(new ArrayList<>());
            }
            for (BookQuoteDto quoteDto : userBookDto.getQuotes()) {
                BookQuoteEntity quoteEntity = new BookQuoteEntity();
                quoteEntity.setPageNumber(quoteDto.getPageNumber());
                quoteEntity.setQuoteText(quoteDto.getQuoteText());
                // 자식 엔티티에 반드시 부모 설정
                quoteEntity.setUserBook(existingEntity);
                existingEntity.getQuotes().add(quoteEntity);
            }
        }

        // 4. 저장
        userBookRepository.save(existingEntity);
        return mapper.map(existingEntity, UserBookDto.class);
    }

}
