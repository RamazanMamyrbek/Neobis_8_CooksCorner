package com.neobis.cookscorner.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_saves", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "recipe_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSaves {
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
        return "UserSaves{" +
                "id=" + id +
                ", userId=" + user.getId() +
                ", recipeId=" + recipe.getId() +
                '}';
    }
}
