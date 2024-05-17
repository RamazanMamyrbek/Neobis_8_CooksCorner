package com.neobis.cookscorner.dtos.user;

import com.neobis.cookscorner.dtos.recipe.RecipeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String photoLink;
    private String description;
    private List<RecipeResponseDto> recipes;
    private Integer likesCount;
    private List<RecipeResponseDto> saves;
    private Integer followersCount;
    private Integer followingsCount;
}
