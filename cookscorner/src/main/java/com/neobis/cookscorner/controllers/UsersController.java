package com.neobis.cookscorner.controllers;

import com.neobis.cookscorner.dtos.user.UserResponseDto;
import com.neobis.cookscorner.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Controller for users")
public class UsersController {
    private final UserService userService;
    @GetMapping("/me")
    @Operation(summary = "Currect user's info")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> myInfo(Principal principal) {
        UserResponseDto userResponseDto = userService.findUserForProfile(principal.getName());
        return ResponseEntity.ok(userResponseDto);
    }
}
