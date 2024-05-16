package com.neobis.cookscorner.dtos.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDto {
    @Schema(example = "Chicken")
    private String name;
    @Schema(example = "1 kg")
    private String quantity;
}
