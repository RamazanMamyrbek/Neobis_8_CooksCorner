package com.neobis.cookscorner.services.impl;

import com.neobis.cookscorner.entities.User;
import com.neobis.cookscorner.services.ApiUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiUserDetailsServiceImpl implements ApiUserDetailsService {
    public final UserServiceImpl userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.findUserByEmail(username);
        if(!user.isPresent()) {
            throw new UsernameNotFoundException(String.format("Username with email %s not found" , username));
        }
        return user.get();
    }
}
