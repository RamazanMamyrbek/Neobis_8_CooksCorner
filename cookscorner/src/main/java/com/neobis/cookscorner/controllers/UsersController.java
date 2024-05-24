package com.neobis.cookscorner.controllers;

import com.neobis.cookscorner.dtos.user.UserEditDto;
import com.neobis.cookscorner.dtos.user.UserResponseDto;
import com.neobis.cookscorner.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
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
    public ResponseEntity<?> currentUserInfo(Principal principal) {
        UserResponseDto userResponseDto = userService.findUserForProfile(principal.getName());
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Specific user's info", description = "Endpoint for user's info with userId")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> userInfo(@PathVariable("userId") Long id) {
        UserResponseDto userResponseDto = userService.findUserForProfile(id);
        return ResponseEntity.ok(userResponseDto);
    }

    //TODO: Edit user
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Edit user", description = "Endpoint for edit user's info.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> editUser(@RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "description", required = false) String description,
                                      @RequestParam(name = "photo", required = false) MultipartFile photo,
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

    @GetMapping("/search")
    @Operation(summary = "Search users by name", description = "Returns search results by user name. Parameter name is required")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> findRecipeByTitle(@RequestParam(name = "name", required = true) String name) {
        List<UserResponseDto> recipes = userService.findAllByNameStartsWith(name);
        return ResponseEntity.ok(recipes);
    }

}
