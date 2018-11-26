package com.balamaci.flux.webclientdemo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    public static final User BANNED_USER = new User("banned", "BannedUser");

    @NotNull
    private String username;

    @NotNull
    private String name;

}
