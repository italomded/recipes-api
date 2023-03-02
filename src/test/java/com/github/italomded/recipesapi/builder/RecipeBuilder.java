package com.github.italomded.recipesapi.builder;

import com.github.italomded.recipesapi.domain.recipe.Image;
import com.github.italomded.recipesapi.domain.recipe.Recipe;
import com.github.italomded.recipesapi.domain.recipe.RecipeIngredient;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;

import java.lang.reflect.Field;

public class RecipeBuilder {
    private Recipe recipe;

    private Field creatorUserField;
    private Field idField;

    private RecipeBuilder() {
        try {
            creatorUserField = Recipe.class.getDeclaredField("creatorUser");
            creatorUserField.setAccessible(true);
            idField = Recipe.class.getDeclaredField("ID");
            idField.setAccessible(true);
            recipe = new Recipe();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static RecipeBuilder builder() {
        return new RecipeBuilder();
    }

    public RecipeBuilder withId(Long id) {
        try {
            idField.set(recipe, id);
            return this;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public RecipeBuilder withCreatorUser(ApplicationUser applicationUser) {
        try {
            creatorUserField.set(recipe, applicationUser);
            return this;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public RecipeBuilder withTitle(String title) {
        recipe.setTitle(title);
        return this;
    }

    public RecipeBuilder withDescription(String description) {
        recipe.setDescription(description);
        return this;
    }

    public RecipeBuilder withImage(Image image) {
        recipe.addImage(image);
        return this;
    }

    public RecipeBuilder withRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipe.addRecipeIngredient(recipeIngredient);
        return this;
    }

    public RecipeBuilder withLike(ApplicationUser applicationUser) {
        recipe.likeRecipe(applicationUser);
        return this;
    }

    public Recipe build() {
        return recipe;
    }
}
