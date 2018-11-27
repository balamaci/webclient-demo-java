package com.balamaci.flux.webclientdemo.user.controller;


import com.balamaci.flux.webclientdemo.common.exception.DuplicateEntityException;
import com.balamaci.flux.webclientdemo.user.User;
import com.balamaci.flux.webclientdemo.user.exception.UserNotFoundException;
import com.balamaci.flux.webclientdemo.user.repository.UserRepository;
import com.balamaci.flux.webclientdemo.user.validator.NotBannedUserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Flux<User> retrieveAll() {
        return userRepository.getAllUsers();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new NotBannedUserValidator());
    }

    //Using Flux<String> doesn't work it just appends Strings
    //known issue in Spring
    @GetMapping(value = "/ids", produces = APPLICATION_JSON_VALUE)
    public Mono<List<String>> retrieveAllUserIds() {
        return userRepository.getAllUsers().map(User::getUsername)
                .collectList();
    }

    @GetMapping("/{username}")
    public Mono<User> findById(@PathVariable String username) {
        return userRepository.findUserById(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException(username)));
    }

    @PostMapping
    public Mono<Void> addUser(@Valid @RequestBody User user) {
        return userRepository.getAllUsers()
                .filter((it) -> it.getUsername().equals(user.getUsername()))
                .map(it -> {
                    throw new DuplicateEntityException();
                })
                .thenEmpty(userRepository.addUser(user));
    }

    @ExceptionHandler
    public ResponseEntity handleNotFoundException(UserNotFoundException ex) {
        log.info("Handling User {} not found", ex.getUsername());
        return ResponseEntity.notFound().build();
    }

}
