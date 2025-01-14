package com.redis.redi2read.repositories;


import com.redis.redi2read.controllers.BookController;
import com.redis.redi2read.models.Book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
//public interface BookRepository extends PagingAndSortingRepository<Book, String> {
public interface BookRepository extends CrudRepository<Book, String> {
//    BookController findById(String isbn);
//    int count();

//    void save(Book book);
}