package com.redis.redi2read.controllers;

import com.redis.redi2read.models.Book;
import com.redis.redi2read.models.Category;
import com.redis.redi2read.repositories.BookRepository;
import com.redis.redi2read.repositories.CategoryRepository;

import com.redislabs.lettusearch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public Iterable<Book> all() {
        return bookRepository.findAll();
    }

    @GetMapping("/categories")
    public Iterable<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{isbn}")
    public Book get(@PathVariable("isbn") String isbn) {
        return bookRepository.findById(isbn).get();
    }

    @Value("${app.booksSearchIndexName}")
    private String searchIndexName;

    @Autowired
    private StatefulRediSearchConnection<String, String> searchConnection;

    @GetMapping("/search")
    @Cacheable("book-search")
    public SearchResults<String,String> search(@RequestParam(name="q")String query) {
        RediSearchCommands<String, String> commands = searchConnection.sync();
        SearchResults<String, String> results = commands.search(searchIndexName, query);
        return results;
    }

    @Value("${app.autoCompleteKey}")
    private String autoCompleteKey;

    @GetMapping("/authors")
    public List<Suggestion<String>> authorAutoComplete(@RequestParam(name="q")String query) {
        RediSearchCommands<String, String> commands = searchConnection.sync();
        SuggetOptions options = SuggetOptions.builder().max(20L).build();
        return commands.sugget(autoCompleteKey, query, options);
    }

}