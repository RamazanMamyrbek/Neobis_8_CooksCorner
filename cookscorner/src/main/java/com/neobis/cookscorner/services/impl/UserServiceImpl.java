package com.neobis.cookscorner.services.impl;

import com.neobis.cookscorner.dtos.RecipeResponseDto;
import com.neobis.cookscorner.dtos.user.UserResponseDto;
import com.neobis.cookscorner.entities.Recipe;
import com.neobis.cookscorner.entities.User;
import com.neobis.cookscorner.entities.UserSaves;
import com.neobis.cookscorner.exceptions.ApiCommonException;
import com.neobis.cookscorner.repositories.RecipeRepository;
import com.neobis.cookscorner.repositories.UserRepository;
import com.neobis.cookscorner.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RecipeRepository recipeRepository;


    @Override
    public Optional<User> findUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public UserResponseDto findUserForProfile(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        userResponseDto.setRecipes(createRecipeResponseDtoListFromRecipes(user.getRecipes()));
        userResponseDto.setSaves(createRecipeResponseDtoListFromUserSaves(user.getSaves()));
        userResponseDto.setLikesCount(user.getLikes().size());
        userResponseDto.setFollowersCount(user.getFollowers().size());
        userResponseDto.setFollowingsCount(user.getFollowings().size());
        return userResponseDto;
    }

    private List<RecipeResponseDto> createRecipeResponseDtoListFromRecipes(List<Recipe> recipes) {
        List<RecipeResponseDto> recipeResponseDtos = new ArrayList<>();
        for(Recipe recipe: recipes) {
            RecipeResponseDto recipeResponseDto = modelMapper.map(recipe, RecipeResponseDto.class);
            recipeResponseDto.setLikesCount(recipe.getLikes().size());
            recipeResponseDto.setSavesCount(recipe.getSaves().size());
            recipeResponseDtos.add(recipeResponseDto);
        }
        return recipeResponseDtos;
    }

    private List<RecipeResponseDto> createRecipeResponseDtoListFromUserSaves(List<UserSaves> saves) {
        List<RecipeResponseDto> recipeResponseDtos = new ArrayList<>();
        for(UserSaves userSave: saves) {
            Recipe recipe = userSave.getRecipe();
            RecipeResponseDto recipeResponseDto = modelMapper.map(recipe, RecipeResponseDto.class);
            recipeResponseDto.setLikesCount(recipe.getLikes().size());
            recipeResponseDto.setSavesCount(recipe.getSaves().size());
            recipeResponseDtos.add(recipeResponseDto);
        }
        return recipeResponseDtos;
    }

}
