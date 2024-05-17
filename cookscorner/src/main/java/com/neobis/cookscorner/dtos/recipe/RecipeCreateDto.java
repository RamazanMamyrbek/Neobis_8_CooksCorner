package com.neobis.cookscorner.dtos.recipe;

import com.neobis.cookscorner.dtos.recipe.IngredientDto;
import com.neobis.cookscorner.entities.Category;
import com.neobis.cookscorner.entities.Difficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeCreateDto {
    @Schema(example = "Chicken ")
    private String title;

    @Schema(example = "You pick up your palette knife and then work that into. Give...")
    private String description;

    private List<IngredientDto> ingredientsList;

    private Difficulty difficulty;

    private Category category;

    @Schema(example = "youtube_url")
    private String youtubeLink;

    @Schema(example = "20-30 min")
    private String preparationTime;
}
