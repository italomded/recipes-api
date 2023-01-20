package com.github.italomded.recipesapi.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(of = {"ID"})
@NoArgsConstructor
@Entity(name = "user")
public class ApplicationUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Getter
    private Long ID;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_like_recipe",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private Set<Recipe> likedRecipes = new HashSet<>();
    @OneToMany(mappedBy = "creatorUser", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Recipe> recipesCreated  = new HashSet<>();

    public void likeRecipe(Recipe recipe) {
        boolean removed = likedRecipes.remove(recipe);
        if (!removed) likedRecipes.add(recipe);
    }
}
