package com.redis.redi2read.controllers;

import com.redis.redi2read.models.User;
import com.redis.redi2read.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Iterable<User> all(@RequestParam(defaultValue = "", name = "email", required = false) String email) {
        log.info("Received request with email: {}", email);
        if (email == null || email.isEmpty()) {
            log.info("Fetching all users");
            return userRepository.findAll();
        } else {
            log.info("Fetching user by email: {}", email);
            Optional<User> user = Optional.ofNullable(userRepository.findFirstByEmail(email));
            log.info("User fetched: {}", user);
            return user.map(List::of).orElse(Collections.emptyList());
        }
    }

}