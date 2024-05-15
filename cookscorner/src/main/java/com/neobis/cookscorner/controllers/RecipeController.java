package com.neobis.cookscorner.controllers;

import com.neobis.cookscorner.dtos.RecipeCreateDto;
import com.neobis.cookscorner.entities.Recipe;
import com.neobis.cookscorner.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        List<Recipe> recipes = recipeService.findAllByCategory(category);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{recipeId}")
    @Operation(summary = "Find recipe by id", description = "Returns a recipe by id.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> findRecipeById(@PathVariable("recipeId") Long recipeId) {
        Recipe recipe = recipeService.findById(recipeId);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/search")
    @Operation(summary = "Search recipes by title", description = "Returns search results by title. Parameter title is required")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> findRecipeByTitle(@RequestParam(name = "title", required = true) String title) {
        List<Recipe> recipes = recipeService.findAllByTitleStartsWith(title);
        return ResponseEntity.ok(recipes);
    }

    @PostMapping()
    @Operation(summary = "Create recipe", description = "Creates a recipe.")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> createRecipe(@RequestBody RecipeCreateDto recipeCreateDto, Principal principal) {
        recipeService.saveRecipe(recipeCreateDto, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Recipe is created successfully."));
    }
}
