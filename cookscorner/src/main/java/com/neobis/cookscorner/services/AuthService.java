package com.neobis.cookscorner.services;

import com.neobis.cookscorner.dtos.user.UserLoginDto;
import com.neobis.cookscorner.dtos.user.UserRegisterDto;
import org.springframework.validation.BindingResult;

public interface AuthService {
    String registerUser(UserRegisterDto userRegisterDto, BindingResult bindingResult);

    String loginUser(UserLoginDto userLoginDto);
}
