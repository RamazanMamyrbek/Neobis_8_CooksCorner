package com.neobis.cookscorner.controllers;

import com.neobis.cookscorner.dtos.recipe.RecipeCreateDto;
import com.neobis.cookscorner.dtos.recipe.RecipeResponseDto;
import com.neobis.cookscorner.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
@Tag(name = "Recipes", description = "Controller for recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    @Operation(summary = "Find recipes by category", description = "Returns all recipes by category. Parameter category is required")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> findAllRecipesByCategory(@RequestParam(name = "category", required = true) String category) {
        List<RecipeResponseDto> recipes = recipeService.findAllByCategory(category);

        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{recipeId}")
    @Operation(summary = "Find recipe by id", description = "Returns a recipe by id.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> findRecipeById(@PathVariable("recipeId") Long recipeId) {
        RecipeResponseDto recipeResponseDto = recipeService.findById(recipeId);
        return ResponseEntity.ok(recipeResponseDto);
    }

    @GetMapping("/search")
    @Operation(summary = "Search recipes by title", description = "Returns search results by title. Parameter title is required")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> findRecipeByTitle(@RequestParam(name = "title", required = true) String title) {
        List<RecipeResponseDto> recipes = recipeService.findAllByTitleStartsWith(title);
        return ResponseEntity.ok(recipes);
    }

//    @PostMapping("/create-info")
//    @Operation(summary = "Create recipe", description = "Creates a recipe with json info.")
//    @SecurityRequirement(name = "JWT")
//    public ResponseEntity<?> createRecipe(@RequestBody RecipeCreateDto recipeCreateDto, Principal principal) {
//        Long id = recipeService.saveRecipeInfo(recipeCreateDto, principal.getName());
//        return ResponseEntity.ok(Map.of("message", "Recipe is created successfully.", "recipeId", id));
//    }
//
//    @PostMapping(value = "/create-photo", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    @Operation(summary = "Create recipe photo", description = "Adds a photo to recipe.")
//    @SecurityRequirement(name = "JWT")
//    public ResponseEntity<?> createRecipe(@RequestParam MultipartFile photo, Principal principal) {
//        recipeService.saveRecipePhoto(photo, principal.getName());
//        return ResponseEntity.ok(Map.of("message", "Recipe is created successfully."));
//    }

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Create recipe photo", description = "Adds a photo to recipe.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> createRecipe(@RequestPart RecipeCreateDto recipeCreateDto,
                                          @RequestPart MultipartFile photo,
                                          Principal principal) {
        recipeService.saveRecipe(recipeCreateDto,photo, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe is created successfully."));
    }

    @GetMapping("/likes/add/{recipeId}")
    @Operation(summary = "Like recipe", description = "Endpoint to like the recipe. Requires recipe id")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> likeRecipe(@PathVariable("recipeId") Long recipeId, Principal principal) {
        recipeService.likeRecipe(recipeId, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe successfully liked"));
    }

    @GetMapping("/likes/remove/{recipeId}")
    @Operation(summary = "Unlike recipe", description = "Endpoint to Unlike the recipe. Requires recipe id")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> unlikeRecipe(@PathVariable("recipeId") Long recipeId, Principal principal) {
        recipeService.unlikeRecipe(recipeId, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe successfully unliked"));
    }

    @GetMapping("/saved/add/{recipeId}")
    @Operation(summary = "Add recipe to saved", description = "Endpoint to save the recipe for user. Requires recipe id")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> addRecipeToSaves(@PathVariable("recipeId") Long recipeId, Principal principal) {
        recipeService.addRecipeToSaves(recipeId, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe successfully added to saved"));
    }

    @GetMapping("/saved/remove/{recipeId}")
    @Operation(summary = "Remove recipe from saves", description = "Endpoint to remove the recipe from user's saved. Requires recipe id")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> removeRecipeFromSaves(@PathVariable("recipeId") Long recipeId, Principal principal) {
        recipeService.removeRecipeFromSaves(recipeId, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe successfully removed from saved"));
    }
}
