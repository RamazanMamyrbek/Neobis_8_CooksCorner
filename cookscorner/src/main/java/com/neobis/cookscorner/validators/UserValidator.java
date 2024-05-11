package com.neobis.cookscorner.validators;

import com.neobis.cookscorner.entities.User;
import com.neobis.cookscorner.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserService userService;
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if(userService.findUserByEmail(((User) target).getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already taken");
        }
    }
}
