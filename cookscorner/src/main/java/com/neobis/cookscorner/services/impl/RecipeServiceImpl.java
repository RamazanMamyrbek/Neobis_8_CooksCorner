package com.neobis.cookscorner.services.impl;

import com.neobis.cookscorner.dtos.recipe.RecipeCreateDto;
import com.neobis.cookscorner.dtos.recipe.RecipeResponseDto;
import com.neobis.cookscorner.entities.*;
import com.neobis.cookscorner.exceptions.ApiCommonException;
import com.neobis.cookscorner.repositories.IngredientRepository;
import com.neobis.cookscorner.repositories.RecipeRepository;
import com.neobis.cookscorner.repositories.UserLikesRepository;
import com.neobis.cookscorner.repositories.UserSavesRepository;
import com.neobis.cookscorner.services.RecipeService;
import com.neobis.cookscorner.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final IngredientRepository ingredientRepository;
    private final UserLikesRepository userLikesRepository;
    private final UserSavesRepository userSavesRepository;
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
            recipeResponseDto.setSavesCount(recipe.getSaves().size());
            recipeResponseDto.setUserId(recipe.getId());
            recipeResponseDtos.add(recipeResponseDto);
        }
    }

    @Override
    public RecipeResponseDto findById(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ApiCommonException("Recipe not found"));
        RecipeResponseDto recipeResponseDto = modelMapper.map(recipe, RecipeResponseDto.class);
        recipeResponseDto.setLikesCount(recipe.getLikes().size());
        recipeResponseDto.setSavesCount(recipe.getSaves().size());
        return recipeResponseDto;
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
    public void saveRecipe(RecipeCreateDto recipeCreateDto, MultipartFile photo, String email) {
        try {
            User user = userService.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
            Recipe recipe = modelMapper.map(recipeCreateDto, Recipe.class);
            recipe.setUser(user);
            //TODO: SAVE RECIPE PHOTO
            if(photo == null) {
                throw new ApiCommonException("Photo should not be null");
            }
            if(!photo.getContentType().equals("image/png")) {
                throw new ApiCommonException("Photo should be in png format");
            }
            String photoUrl = savePhoto(photo);
            recipe.setPhotoLink(photoUrl);
            user.getRecipes().add(recipe);
            recipe.getIngredients().stream().forEach(ingredient -> ingredient.setRecipe(recipe));
            recipeRepository.save(recipe);
            ingredientRepository.saveAll(recipe.getIngredients());
        } catch (IOException e) {
            throw new ApiCommonException("Error in photo saving");
        }
    }

    private String savePhoto(MultipartFile photo) throws IOException {
        // Генерация пути к файлу и сохранение
        Path uploadPath = Paths.get("src/main/resources/photos");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Возвращаем путь к файлу
        return "http://localhost:8080/api/photos/" + filename;
    }

//    @Transactional
//    @Override
//    public Long saveRecipeInfo(RecipeCreateDto recipeCreateDto, String email) {
//        User user = userService.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
//        Recipe recipe = modelMapper.map(recipeCreateDto, Recipe.class);
//        recipe.setUser(user);
//        user.getRecipes().add(recipe);
//        recipe.getIngredients().stream().forEach(ingredient -> ingredient.setRecipe(recipe));
//        recipeRepository.save(recipe);
//        ingredientRepository.saveAll(recipe.getIngredients());
//        return recipe.getId();
//    }
//
//    @Transactional
//    @Override
//    public void saveRecipePhoto(MultipartFile photo, String email) {
//        User user = userService.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
////        Recipe recipe =
//    }

    @Transactional
    @Override
    public void likeRecipe(Long recipeId, String email) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ApiCommonException("Recipe not found"));
        User user = userService.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
        UserLikes userLikes = UserLikes
                .builder()
                .recipe(recipe)
                .user(user)
                .build();
        userLikesRepository.save(userLikes);
    }

    @Transactional
    @Override
    public void unlikeRecipe(Long recipeId, String email) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ApiCommonException("Recipe not found"));
        User user = userService.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
        userLikesRepository.deleteUserLikesByUserAndRecipe(user, recipe);
    }

    @Transactional
    @Override
    public void addRecipeToSaves(Long recipeId, String email) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ApiCommonException("Recipe not found"));
        User user = userService.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
        UserSaves userSaves = UserSaves
                .builder()
                .recipe(recipe)
                .user(user)
                .build();
        userSavesRepository.save(userSaves);
    }

    @Transactional
    @Override
    public void removeRecipeFromSaves(Long recipeId, String email) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ApiCommonException("Recipe not found"));
        User user = userService.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
        userSavesRepository.deleteUserSavesByUserAndRecipe(user, recipe);
    }


}
