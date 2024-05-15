package com.neobis.cookscorner.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.attribute.standard.MediaSize;
import java.util.List;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;


    private String description;

    private String photo;

    @OneToMany(mappedBy = "recipe")
    @JsonIgnore
    private List<Ingredient> ingredients;


    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;


    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "preparation_time")
    private String preparationTime;

    @Column(name = "youtube_link")
    private String youtubeLink;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "recipe")
    private List<UserLikes> likes;

    @OneToMany(mappedBy = "recipe")
    private List<UserSaves> saves;

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", photo='" + photo + '\'' +
                ", ingredients=" + ingredients +
                ", difficulty=" + difficulty +
                ", category=" + category +
                ", preparationTime='" + preparationTime + '\'' +
                ", youtubeLink='" + youtubeLink + '\'' +
                ", userId=" + user.getId() +
                ", likes=" + likes +
                '}';
    }
}
