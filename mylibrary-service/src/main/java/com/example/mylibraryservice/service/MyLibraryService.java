// src/main/java/com/example/mylibraryservice/service/MyLibraryService.java
package com.example.mylibraryservice.service;

import com.example.mylibraryservice.dto.UserBookDto;
import java.util.List;

public interface MyLibraryService {
    UserBookDto createUserBook(UserBookDto userBookDto);
    List<UserBookDto> getUserBooks(String userId);
    UserBookDto getUserBook(String userId, Long bookId);

}
