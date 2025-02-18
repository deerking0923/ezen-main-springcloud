package com.example.mylibraryservice.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserBookRepository extends CrudRepository<UserBookEntity, Long> {
    List<UserBookEntity> findByUserId(String userId);
}