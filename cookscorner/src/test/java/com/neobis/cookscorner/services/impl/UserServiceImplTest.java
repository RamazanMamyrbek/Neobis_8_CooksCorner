package com.neobis.cookscorner.services.impl;

import com.neobis.cookscorner.dtos.user.UserResponseDto;
import com.neobis.cookscorner.entities.User;
import com.neobis.cookscorner.exceptions.ApiCommonException;
import com.neobis.cookscorner.repositories.RecipeRepository;
import com.neobis.cookscorner.repositories.UserRepository;
import com.neobis.cookscorner.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final String existingEmail = "test1@mail.com";
    private final String nonExistingEmail = "test2@mail.com";
    private final Long existingId = 1L;
    private final Long nonExistingId = 2L;

    private User user;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        user = User
                .builder().
                id(1L).name("user").email("user@mail.com")
                .password("password").photoLink("")
                .enabled(true).recipes(new ArrayList<>())
                .likes(new ArrayList<>()).saves(new ArrayList<>())
                .followers(new ArrayList<>()).followings(new ArrayList<>())
                .build();

        userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(existingEmail);
    }

    @Test
    void testFindUserByExistingEmail() {
        doReturn(Optional.of(user))
                .when(userRepository).findUserByEmail(existingEmail);

        Optional<User> actualResult = userService.findUserByEmail(existingEmail);

        assertTrue(actualResult.isPresent());
    }

    @Test
    void testFindUserByNonExistingEmail() {
        doReturn(Optional.empty())
                .when(userRepository).findUserByEmail(nonExistingEmail);

        Optional<User> actualResult = userService.findUserByEmail(nonExistingEmail);

        assertFalse(actualResult.isPresent());
    }

    @Test
    void testFindUserForProfileByEmail() {
        when(userRepository.findUserByEmail(existingEmail)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);

        UserResponseDto actualResult = userService.findUserForProfile(existingEmail);

        assertNotNull(actualResult);
        assertEquals(existingEmail, actualResult.getEmail());
    }

    @Test
    void testFindUserForProfileByEmail_UserNotFound() {
        when(userRepository.findUserByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        ApiCommonException exception = assertThrows(ApiCommonException.class, () -> {
            userService.findUserForProfile(nonExistingEmail);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testFindUserForProfileById() {
        when(userRepository.findById(existingId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);

        UserResponseDto actualResult = userService.findUserForProfile(existingId);

        assertNotNull(actualResult);
        assertEquals(existingEmail, actualResult.getEmail());
    }

    @Test
    void testFindUserForProfileById_UserNotFound() {
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        ApiCommonException exception = assertThrows(ApiCommonException.class, () -> {
            userService.findUserForProfile(nonExistingId);
        });

        assertEquals("User not found", exception.getMessage());
    }

    // Additional tests can be written for other methods in UserServiceImpl as needed
}
