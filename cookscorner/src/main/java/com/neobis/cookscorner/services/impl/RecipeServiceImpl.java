package com.neobis.cookscorner.services.impl;

import com.neobis.cookscorner.dtos.RecipeCreateDto;
import com.neobis.cookscorner.dtos.RecipeResponseDto;
import com.neobis.cookscorner.entities.Category;
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
    public List<RecipeResponseDto> findAllByCategory(String category) {
        List<RecipeResponseDto> recipeResponseDtos = new ArrayList<>();
        if(category.toUpperCase().equals(Category.BREAKFAST.toString())) {
            List<Recipe> recipes = recipeRepository.findAllByCategory(Category.BREAKFAST);
            fillRecipeResponseDtos(recipeResponseDtos, recipes);
        } else if(category.toUpperCase().equals(Category.LUNCH.toString())) {
            List<Recipe> recipes = recipeRepository.findAllByCategory(Category.LUNCH);
            fillRecipeResponseDtos(recipeResponseDtos, recipes);
        } else if(category.toUpperCase().equals(Category.DINNER.toString())) {
            List<Recipe> recipes = recipeRepository.findAllByCategory(Category.DINNER);
            fillRecipeResponseDtos(recipeResponseDtos, recipes);
        }

        return recipeResponseDtos;
    }

    private void fillRecipeResponseDtos(List<RecipeResponseDto> recipeResponseDtos, List<Recipe> recipes) {
        for(Recipe recipe: recipes) {
            int likeCount = 0;
            RecipeResponseDto recipeResponseDto = modelMapper.map(recipe, RecipeResponseDto.class);
            recipeResponseDto.setLikesCount(recipe.getLikes().size());
            recipeResponseDto.setUserId(recipe.getId());
            recipeResponseDtos.add(recipeResponseDto);
        }
    }

    @Override
    public RecipeResponseDto findById(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ApiCommonException("Recipe not found"));
        return modelMapper.map(recipe, RecipeResponseDto.class);
    }

    @Override
    public List<RecipeResponseDto> findAllByTitleStartsWith(String title) {
        List<Recipe> recipes = recipeRepository.findAllByTitleStartsWithIgnoreCase(title);
        List<RecipeResponseDto> recipeResponseDtos = new ArrayList<>();
        fillRecipeResponseDtos(recipeResponseDtos, recipes);
        return recipeResponseDtos;
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
