package com.github.italomded.recipesapi.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode(of = {"ID"})
@Entity(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long ID;

    @JoinColumn(nullable = false)
    @ManyToOne
    @Getter
    @Setter
    private ApplicationUser creatorUser;

    @Column(nullable = false)
    @OneToMany(mappedBy = "recipe", orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter
    private Set<Image> images = new HashSet<>();
    @Column(nullable = false, length = 50)
    @Getter
    @Setter
    private String title;
    @Column(length = 500)
    @Getter
    @Setter
    private String description;

    @Column(nullable = false)
    @OneToMany(mappedBy = "recipe", orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter
    private List<RecipeIngredient> ingredientsOfRecipe = new ArrayList<>();
    @ManyToMany(mappedBy = "likedRecipes", fetch = FetchType.LAZY)
    private Set<ApplicationUser> usersWhoLiked = new HashSet<>();

    public Recipe(String title, String description) {
        this.images = new HashSet<>();
        this.title = title;
        this.description = description;
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public void removeImage(Image image) {
        images.remove(image);
    }

    public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
        int amountOfIngredients = ingredientsOfRecipe.size();
        if (recipeIngredient.getSequence() == null || recipeIngredient.getSequence() > amountOfIngredients) {
            recipeIngredient.setSequence(amountOfIngredients + 1);
        } else {
            for (RecipeIngredient ri : ingredientsOfRecipe) {
                if (ri.getSequence() >= recipeIngredient.getSequence()) {
                    ri.setSequence(ri.getSequence() + 1);
                }
            }
        }
        ingredientsOfRecipe.add(recipeIngredient);
    }

    public void removeRecipeIngredient(RecipeIngredient recipeIngredient) {
        for (RecipeIngredient ri : ingredientsOfRecipe) {
            if (ri.getSequence() > recipeIngredient.getSequence()) {
                ri.setSequence(ri.getSequence() - 1);
            }
        }
        ingredientsOfRecipe.remove(recipeIngredient);
    }

    public void addLike(ApplicationUser applicationUser) {
        usersWhoLiked.add(applicationUser);
    }

    public void removeLike(ApplicationUser applicationUser) {
        usersWhoLiked.remove(applicationUser);
    }

    public Integer getLikes() {
        return usersWhoLiked.size();
    }
}
