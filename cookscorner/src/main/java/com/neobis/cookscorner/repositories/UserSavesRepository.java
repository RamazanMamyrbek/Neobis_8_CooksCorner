package com.neobis.cookscorner.repositories;

import com.neobis.cookscorner.entities.Recipe;
import com.neobis.cookscorner.entities.User;
import com.neobis.cookscorner.entities.UserSaves;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSavesRepository extends JpaRepository<UserSaves, Long> {
    void deleteUserSavesByUserAndRecipe(User user, Recipe recipe);
}
