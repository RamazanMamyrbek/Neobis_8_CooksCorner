package com.neobis.cookscorner.repositories;

import com.neobis.cookscorner.entities.Category;
import com.neobis.cookscorner.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByCategory(Category category);
    List<Recipe> findAllByTitleStartsWithIgnoreCase(String title);
}
