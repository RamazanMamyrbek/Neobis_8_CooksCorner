package com.neobis.cookscorner.services.impl;

import com.neobis.cookscorner.dtos.IngredientDto;
import com.neobis.cookscorner.dtos.RecipeCreateDto;
import com.neobis.cookscorner.entities.Category;
import com.neobis.cookscorner.entities.Ingredient;
import com.neobis.cookscorner.entities.Recipe;
import com.neobis.cookscorner.entities.User;
import com.neobis.cookscorner.exceptions.ApiCommonException;
import com.neobis.cookscorner.repositories.IngredientRepository;
import com.neobis.cookscorner.repositories.RecipeRepository;
import com.neobis.cookscorner.services.RecipeService;
import com.neobis.cookscorner.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final IngredientRepository ingredientRepository;
    @Override
    public List<Recipe> findAllByCategory(String category) {
        if(category.toUpperCase().equals(Category.BREAKFAST.toString()))
            return recipeRepository.findAllByCategory(Category.BREAKFAST);
        else if(category.toUpperCase().equals(Category.LUNCH.toString()))
            return recipeRepository.findAllByCategory(Category.LUNCH);
        else if(category.toUpperCase().equals(Category.DINNER.toString()))
            return recipeRepository.findAllByCategory(Category.DINNER);
        return Collections.emptyList();
    }

    @Override
    public Recipe findById(Long recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(() -> new ApiCommonException("Recipe not found"));
    }

    @Override
    public List<Recipe> findAllByTitleStartsWith(String title) {
        return recipeRepository.findAllByTitleStartsWithIgnoreCase(title);
    }

    @Transactional
    @Override
    public void saveRecipe(RecipeCreateDto recipeCreateDto, String email) {
        User user = userService.findUserByEmail(email).get();
        Recipe recipe = modelMapper.map(recipeCreateDto, Recipe.class);
        recipe.setUser(user);
        user.getRecipes().add(recipe);
        recipe.getIngredients().stream().forEach(ingredient -> ingredient.setRecipe(recipe));
        recipeRepository.save(recipe);
        ingredientRepository.saveAll(recipe.getIngredients());
    }
}
