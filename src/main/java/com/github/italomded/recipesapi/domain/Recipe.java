package com.github.italomded.recipesapi.domain;

import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

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
    private ApplicationUser creatorUser;

    @Column(nullable = false)
    @OneToMany(mappedBy = "recipe", orphanRemoval = true, fetch = FetchType.LAZY)
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
    private List<RecipeIngredient> ingredientsOfRecipe = new ArrayList<>();
    @ManyToMany(mappedBy = "likedRecipes", fetch = FetchType.LAZY)
    private Set<ApplicationUser> usersWhoLiked = new HashSet<>();

    public Recipe(String title, String description, ApplicationUser creatorUser) {
        this.images = new HashSet<>();
        this.title = title;
        this.description = description;
        this.creatorUser = creatorUser;
    }

    public Set<Image> getImages() {
        return Collections.unmodifiableSet(images);
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public boolean removeImage(Image image) {
        if (!images.contains(image)) return false;
        if (images.size() < 2) return false;
        images.remove(image);
        return true;
    }

    public List<RecipeIngredient> getIngredientsOfRecipe() {
        return Collections.unmodifiableList(ingredientsOfRecipe);
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

    public boolean removeRecipeIngredient(RecipeIngredient recipeIngredient) {
        if (!ingredientsOfRecipe.contains(recipeIngredient)) return false;
        if (ingredientsOfRecipe.size() < 4) return false;
        for (RecipeIngredient ri : ingredientsOfRecipe) {
            if (ri.getSequence() > recipeIngredient.getSequence()) {
                ri.setSequence(ri.getSequence() - 1);
            }
        }
        ingredientsOfRecipe.remove(recipeIngredient);
        return true;
    }

    public void likeRecipe(ApplicationUser applicationUser) {
        boolean removed = usersWhoLiked.remove(applicationUser);
        if (!removed) usersWhoLiked.add(applicationUser);
    }

    public Integer getLikes() {
        return usersWhoLiked.size();
    }

    public Set<ApplicationUser> getUsersThatLiked() {
        return Collections.unmodifiableSet(usersWhoLiked);
    }
}
