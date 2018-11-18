package com.balamaci.flux.webclientdemo.user.controller;


import com.balamaci.flux.webclientdemo.user.User;
import com.balamaci.flux.webclientdemo.user.exception.BannedUserException;
import com.balamaci.flux.webclientdemo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Flux<User> retrieveAll() {
        return userRepository.getAllUsers();
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
        if(username.equals(User.BANNED_USER.getUsername())) {
            throw new BannedUserException();
        }

        return userRepository.findUserById(username);
    }


}
