package com.neobis.cookscorner.services;

import com.neobis.cookscorner.dtos.recipe.RecipeCreateDto;
import com.neobis.cookscorner.dtos.recipe.RecipeResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    List<RecipeResponseDto> findAllByCategory(String category);

    RecipeResponseDto findById(Long recipeId);

    List<RecipeResponseDto> findAllByTitleStartsWith(String title);

    void saveRecipe(RecipeCreateDto recipeCreateDto, MultipartFile photo, String email);
//    Long saveRecipeInfo(RecipeCreateDto recipeCreateDto, String email);
//    void saveRecipePhoto(MultipartFile photo, String email);

    void likeRecipe(Long recipeId, String name);

    void unlikeRecipe(Long recipeId, String name);

    void addRecipeToSaves(Long recipeId, String name);

    void removeRecipeFromSaves(Long recipeId, String name);



}
