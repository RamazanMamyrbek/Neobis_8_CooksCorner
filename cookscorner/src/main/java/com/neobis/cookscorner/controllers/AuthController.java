package com.neobis.cookscorner.controllers;

import com.neobis.cookscorner.dtos.user.UserLoginDto;
import com.neobis.cookscorner.dtos.user.UserRegisterDto;

import com.neobis.cookscorner.dtos.user.UserResponseDto;
import com.neobis.cookscorner.services.AuthService;
import com.neobis.cookscorner.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/auth")
@Tag(name = "Auth", description = "Controller for authentication/authorization")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Endpoint for user registration. Returns jwt token.")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto userRegisterDto,
                                      BindingResult bindingResult) {
        String jwtToken = authService.registerUser(userRegisterDto, bindingResult);
        return ResponseEntity.ok(Map.of("token", jwtToken));
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Endpoint for user login. Returns jwt token.")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto) {
        String jwtToken = authService.loginUser(userLoginDto);
        UserResponseDto userResponseDto = userService.findUserForProfile(userLoginDto.getEmail());
        return ResponseEntity.ok(Map.of("token", jwtToken, "user", userResponseDto));
    }


}
