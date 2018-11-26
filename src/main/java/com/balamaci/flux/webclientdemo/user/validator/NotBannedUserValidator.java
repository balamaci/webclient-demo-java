package com.balamaci.flux.webclientdemo.user.validator;

import com.balamaci.flux.webclientdemo.user.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author sbalamaci
 */
@Component
public class NotBannedUserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object entity, Errors errors) {
        User user = (User) entity;
        if(User.BANNED_USER.getUsername().equals(user.getUsername())) {
            errors.rejectValue("username", "usernameBanned",
                    new Object[] { user.getUsername()},"Username is banned");
        }
    }
}
