package com.neobis.cookscorner.repositories;

import com.neobis.cookscorner.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
