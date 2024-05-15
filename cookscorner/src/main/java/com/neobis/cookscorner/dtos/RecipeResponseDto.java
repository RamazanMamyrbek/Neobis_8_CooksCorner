package com.neobis.cookscorner.dtos;

import com.neobis.cookscorner.entities.*;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponseDto {
    private Long id;
    private String title;
    private String description;
    private String photo;
    private List<IngredientResponseDto> ingredients;
    private Difficulty difficulty;
    private Category category;
    private String preparationTime;
    private String youtubeLink;
    private Long userId;
    private Integer likesCount;
    private Integer savesCount;
}
