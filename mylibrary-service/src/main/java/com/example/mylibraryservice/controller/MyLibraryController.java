// src/main/java/com/example/mylibraryservice/controller/MyLibraryController.java
package com.example.mylibraryservice.controller;

import com.example.mylibraryservice.dto.UserBookDto;
import com.example.mylibraryservice.service.MyLibraryService;
import com.example.mylibraryservice.vo.RequestUserBook;
import com.example.mylibraryservice.vo.ResponseUserBook;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mylibrary-service")
@Slf4j
public class MyLibraryController {

    private final MyLibraryService myLibraryService;

    public MyLibraryController(MyLibraryService myLibraryService) {
        this.myLibraryService = myLibraryService;
    }

    /**
     * GET /mylibrary-service/{userId}/booklist
     * 해당 유저가 보유한 책 목록 조회
     */
    @GetMapping("/{userId}/booklist")
    public ResponseEntity<?> getUserBooks(@PathVariable("userId") String userId) {
        log.info("GET booklist for userId: {}", userId);
        return ResponseEntity.ok(myLibraryService.getUserBooks(userId));
    }

    /**
     * POST /mylibrary-service/{userId}/create
     * 유저가 UserBooks를 추가
     */
    @PostMapping("/{userId}/create")
    public ResponseEntity<ResponseUserBook> createUserBook(
            @PathVariable("userId") String userId,
            @RequestBody RequestUserBook requestBook
    ) {
        log.info("POST create book for userId: {}", userId);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserBookDto userBookDto = mapper.map(requestBook, UserBookDto.class);
        userBookDto.setUserId(userId); // pathVariable과 dto 동기화

        // 서비스에서 로직 처리
        UserBookDto createdDto = myLibraryService.createUserBook(userBookDto);

        // VO로 변환 후 응답
        ResponseUserBook response = mapper.map(createdDto, ResponseUserBook.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
