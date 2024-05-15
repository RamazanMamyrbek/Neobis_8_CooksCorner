package com.neobis.cookscorner.services;

import com.neobis.cookscorner.dtos.RecipeCreateDto;
import com.neobis.cookscorner.entities.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> findAllByCategory(String category);

    Recipe findById(Long recipeId);

    List<Recipe> findAllByTitleStartsWith(String title);

    void saveRecipe(RecipeCreateDto recipeCreateDto, String email);
}
