package com.neobis.cookscorner.services;

import com.neobis.cookscorner.dtos.RecipeCreateDto;
import com.neobis.cookscorner.dtos.RecipeResponseDto;

import java.util.List;

public interface RecipeService {
    List<RecipeResponseDto> findAllByCategory(String category);

    RecipeResponseDto findById(Long recipeId);

    List<RecipeResponseDto> findAllByTitleStartsWith(String title);

    void saveRecipe(RecipeCreateDto recipeCreateDto, String email);

    void likeRecipe(Long recipeId, String name);

    void unlikeRecipe(Long recipeId, String name);

    void addRecipeToSaves(Long recipeId, String name);

    void removeRecipeFromSaves(Long recipeId, String name);
}
