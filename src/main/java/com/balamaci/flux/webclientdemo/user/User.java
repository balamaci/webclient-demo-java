package com.balamaci.flux.webclientdemo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    public static final User BANNED_USER = new User("banned", "BannedUser");

    private String username;
    private String name;

}
