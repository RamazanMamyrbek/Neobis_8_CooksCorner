package com.neobis.cookscorner.services;

import com.neobis.cookscorner.dtos.user.UserEditDto;
import com.neobis.cookscorner.dtos.user.UserResponseDto;
import com.neobis.cookscorner.entities.User;

import java.security.Principal;
import java.util.Optional;

public interface UserService {
    Optional<User> findUserByEmail(String email);

    void saveUser(User user);

    UserResponseDto findUserForProfile(String email);

    void editUser(UserEditDto userEditDto, String email);

    void followUser(Long id, String email);

    void unFollowUser(Long id, String email);
}
