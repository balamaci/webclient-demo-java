package com.balamaci.flux.webclientdemo.user.repository;

import com.balamaci.flux.webclientdemo.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Repository
@Slf4j
public class UserRepository {

    private static List<User> users = Arrays.asList(
            new User("john", "John Snow"),
            new User("cersei", "Cersei Lannister"),
            new User("tyrion", "Tyrion Lannister"),
            new User("arya", "Arya Stark")
            );

    public Mono<User> findUserById(String username) {
        log.info("Getting user by username={}", username);
        return Flux.fromIterable(users).filter(user -> user.getUsername().equals(username))
                .singleOrEmpty();
    }

    public void addUser(User user) {
        log.info("Adding {}", user);
        users.add(user);
    }

    public Flux<User> getAllUsers() {
        return Flux.fromIterable(users);
    }

}
