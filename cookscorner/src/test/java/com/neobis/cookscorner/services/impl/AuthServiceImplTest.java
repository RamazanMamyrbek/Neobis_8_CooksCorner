package com.neobis.cookscorner.services.impl;

import com.neobis.cookscorner.dtos.user.UserRegisterDto;
import com.neobis.cookscorner.exceptions.ApiCommonException;
import com.neobis.cookscorner.services.JwtService;
import com.neobis.cookscorner.services.UserService;
import com.neobis.cookscorner.validators.UserValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private static UserService userService;
    @Mock
    private static ModelMapper modelMapper;
    @Mock
    private static UserValidator userValidator;
    @Mock
    private static JwtService jwtService;
    @Mock
    private static AuthenticationManager authenticationManager;
    @Mock
    private static UserDetailsService userDetailsService;
    @Mock
    private static PasswordEncoder passwordEncoder;

    @InjectMocks
    private static AuthServiceImpl authService;

    private static UserRegisterDto userRegisterDtoWithCorrectPassword = new UserRegisterDto("testUser", "user@mail.com", "Password1234.", "Password1234.");
    private static UserRegisterDto userRegisterDtoWithIncorrectPassword = new UserRegisterDto("testUser", "user@mail.com", "Password1234.", "Password12.");

    @Test
    void testRegisterUserWithNotMatchingPasswords() {
        assertThrows(ApiCommonException.class, () -> authService.registerUser(userRegisterDtoWithIncorrectPassword, new BeanPropertyBindingResult(new Object(), "object")));
    }

    @Test
    void testRegisterUserWithExistingEmail() {
        BindingResult errors = new BeanPropertyBindingResult(userRegisterDtoWithCorrectPassword, "user");
        errors.rejectValue("email", "", "This email is already taken");
        assertThrows(ApiCommonException.class, () -> authService.registerUser(userRegisterDtoWithCorrectPassword, errors));
    }


}