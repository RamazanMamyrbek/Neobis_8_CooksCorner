package com.neobis.cookscorner.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Controller for users")
public class UsersController {


    @GetMapping("/testAuth")
    @Operation(summary = "Test security")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> testSecurity(Authentication authentication) {
        return ResponseEntity.ok(Map.of("details", authentication));
    }
}
