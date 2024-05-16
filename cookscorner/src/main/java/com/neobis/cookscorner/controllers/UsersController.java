package com.neobis.cookscorner.controllers;

import com.neobis.cookscorner.dtos.user.UserEditDto;
import com.neobis.cookscorner.dtos.user.UserResponseDto;
import com.neobis.cookscorner.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Controller for users")
public class UsersController {
    private final UserService userService;
    @GetMapping("/me")
    @Operation(summary = "Current user's info")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> userInfo(Principal principal) {
        UserResponseDto userResponseDto = userService.findUserForProfile(principal.getName());
        return ResponseEntity.ok(userResponseDto);
    }

    //TODO: Edit user
    @PutMapping(name = "/edit/{userId}", consumes = {"multipart/form-data"})
    @Operation(summary = "Edit user", description = "Endpoint for edit user's info. Requires userId.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> editUser(@RequestParam("name") String name,
                                      @RequestParam("description") String description,
                                      @RequestParam("photo") MultipartFile photo,
                                      Principal principal) {
        UserEditDto userEditDto = UserEditDto.builder().name(name).description(description).photo(photo).build();
        userService.editUser(userEditDto, principal.getName());
        return ResponseEntity.ok(Map.of("message", "User edited"));
    }

    @PostMapping("/followings/add/{userId}")
    @Operation(summary = "Add user to followings", description = "Endpoint for follow a user. Requires user id to follow")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> followUser(@PathVariable("userId") Long id, Principal principal) {
        userService.followUser(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "User added to followings"));
    }

    @PostMapping("/followings/remove/{userId}")
    @Operation(summary = "Remove user from followings", description = "Endpoint for removing a user from followings . Requires user id to follow")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> unFollowUser(@PathVariable("userId") Long id, Principal principal) {
        userService.unFollowUser(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "User removed from followings"));
    }
}
