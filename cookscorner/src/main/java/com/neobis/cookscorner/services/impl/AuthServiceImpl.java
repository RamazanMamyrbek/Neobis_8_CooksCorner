package com.neobis.cookscorner.services.impl;

import com.neobis.cookscorner.dtos.UserLoginDto;
import com.neobis.cookscorner.dtos.UserRegisterDto;
import com.neobis.cookscorner.entities.User;
import com.neobis.cookscorner.exceptions.ApiCommonException;
import com.neobis.cookscorner.services.AuthService;
import com.neobis.cookscorner.services.JwtService;
import com.neobis.cookscorner.services.UserService;
import com.neobis.cookscorner.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String registerUser(UserRegisterDto userRegisterDto, BindingResult bindingResult) {
        User user = modelMapper.map(userRegisterDto, User.class);
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            StringBuilder message = new StringBuilder();
            for(FieldError error: fieldErrors) {
                message.append(error.getDefaultMessage()).append(", ");
            }
            throw new ApiCommonException(message.toString());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userService.saveUser(user);
        String jwtToken = jwtService.generateToken(user);
        return jwtToken;
    }

    @Override
    public String loginUser(UserLoginDto userLoginDto) {
        String jwtToken;
        try{
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword());
            authenticationManager.authenticate(authenticationToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDto.getEmail());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            jwtToken = jwtService.generateToken(userDetails);

        } catch (BadCredentialsException ex) {
            throw new ApiCommonException("Invalid username or password");
        }

        return jwtToken;
    }
}
