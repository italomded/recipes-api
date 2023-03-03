package com.github.italomded.recipesapi.domain.recipe;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import jakarta.persistence.*;
import lombok.*;

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
    @OrderBy("sequence ASC")
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
        ingredientsOfRecipe.add(recipeIngredient);
        int index = ingredientsOfRecipe.indexOf(recipeIngredient);
        recipeIngredient.setSequence(index + 1);
    }

    public void addRecipeIngredient(RecipeIngredient recipeIngredient, Integer sequence) {
        try {
            int indexToBeAdded = sequence - 1;
            ingredientsOfRecipe.add(indexToBeAdded, recipeIngredient);
            updateSequences(indexToBeAdded);
        } catch (IndexOutOfBoundsException e) {
            addRecipeIngredient(recipeIngredient);
        }
    }

    public boolean removeRecipeIngredient(RecipeIngredient recipeIngredient) {
        int indexToBeRemoved = ingredientsOfRecipe.indexOf(recipeIngredient);
        if (indexToBeRemoved < 0) return false;
        if (ingredientsOfRecipe.size() < 4) return false;

        ingredientsOfRecipe.remove(recipeIngredient);
        updateSequences(indexToBeRemoved);

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

    private void updateSequences(Integer actualIndexRefactor) {
        for (int index = actualIndexRefactor; index < ingredientsOfRecipe.size(); index++) {
            var recipeIngredient = ingredientsOfRecipe.get(index);
            recipeIngredient.setSequence(index + 1);
        }
    }
}
