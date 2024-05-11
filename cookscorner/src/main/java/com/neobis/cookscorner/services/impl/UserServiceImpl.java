package com.neobis.cookscorner.services.impl;

import com.neobis.cookscorner.entities.User;
import com.neobis.cookscorner.exceptions.ApiCommonException;
import com.neobis.cookscorner.repositories.UserRepository;
import com.neobis.cookscorner.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public final UserRepository userRepository;

    @Override
    public Optional<User> findUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
