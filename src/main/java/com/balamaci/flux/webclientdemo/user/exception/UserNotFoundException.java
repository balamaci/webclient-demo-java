package com.balamaci.flux.webclientdemo.user.exception;

/**
 * @author sbalamaci
 */
public class UserNotFoundException extends RuntimeException {

    private String username;

    public UserNotFoundException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
