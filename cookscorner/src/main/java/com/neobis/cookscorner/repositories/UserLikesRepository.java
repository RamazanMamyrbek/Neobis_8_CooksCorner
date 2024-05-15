package com.neobis.cookscorner.repositories;

import com.neobis.cookscorner.entities.Recipe;
import com.neobis.cookscorner.entities.User;
import com.neobis.cookscorner.entities.UserLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLikesRepository extends JpaRepository<UserLikes, Long> {
    void deleteUserLikesByUserAndRecipe(User user, Recipe recipe);
}
