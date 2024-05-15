package com.neobis.cookscorner.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false)
    private Recipe recipe;

    @Override
    public String toString() {
        return "UserLikes{" +
                "id=" + id +
                ", userId=" + user.getId() +
                ", recipeId=" + recipe.getId() +
                '}';
    }
}
