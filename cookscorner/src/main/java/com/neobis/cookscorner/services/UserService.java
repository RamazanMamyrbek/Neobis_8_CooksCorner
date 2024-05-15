package com.neobis.cookscorner.services;

import com.neobis.cookscorner.dtos.user.UserResponseDto;
import com.neobis.cookscorner.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByEmail(String email);

    void saveUser(User user);

    UserResponseDto findUserForProfile(String name);
}
