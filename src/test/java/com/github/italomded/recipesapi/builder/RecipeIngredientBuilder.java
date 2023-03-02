package com.github.italomded.recipesapi.builder;

import com.github.italomded.recipesapi.domain.recipe.Ingredient;
import com.github.italomded.recipesapi.domain.recipe.Recipe;
import com.github.italomded.recipesapi.domain.recipe.RecipeIngredient;

import java.lang.reflect.Field;

public class RecipeIngredientBuilder {
    private RecipeIngredient recipeIngredient;

    private Field recipeField;
    private Field idField;

    private RecipeIngredientBuilder() {
        try {
            idField = RecipeIngredient.class.getDeclaredField("ID");
            idField.setAccessible(true);
            recipeField = RecipeIngredient.class.getDeclaredField("recipe");
            recipeField.setAccessible(true);
            recipeIngredient = new RecipeIngredient();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static RecipeIngredientBuilder builder() {
        return new RecipeIngredientBuilder();
    }

    public RecipeIngredientBuilder withId(Long id) {
        try {
            idField.set(recipeIngredient, id);
            return this;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public RecipeIngredientBuilder withRecipe(Recipe recipe) {
        try {
            recipeField.set(recipeIngredient, recipe);
            return this;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public RecipeIngredient build() {
        return recipeIngredient;
    }
}
