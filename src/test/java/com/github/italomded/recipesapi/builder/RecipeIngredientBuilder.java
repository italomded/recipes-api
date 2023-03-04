package com.github.italomded.recipesapi.builder;

import com.github.italomded.recipesapi.domain.recipe.Ingredient;
import com.github.italomded.recipesapi.domain.recipe.Quantity;
import com.github.italomded.recipesapi.domain.recipe.Recipe;
import com.github.italomded.recipesapi.domain.recipe.RecipeIngredient;

import java.lang.reflect.Field;

public class RecipeIngredientBuilder {
    private RecipeIngredient recipeIngredient;

    private Field recipeField;
    private Field idField;
    private Field ingredientField;
    private Field quantityField;

    private RecipeIngredientBuilder() {
        try {
            idField = RecipeIngredient.class.getDeclaredField("ID");
            idField.setAccessible(true);
            recipeField = RecipeIngredient.class.getDeclaredField("recipe");
            recipeField.setAccessible(true);
            ingredientField = RecipeIngredient.class.getDeclaredField("ingredient");
            ingredientField.setAccessible(true);
            quantityField = RecipeIngredient.class.getDeclaredField("quantity");
            quantityField.setAccessible(true);
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

    public RecipeIngredientBuilder withSequence(Integer sequence) {
        recipeIngredient.setSequence(sequence);
        return this;
    }

    public RecipeIngredientBuilder withIngredient(Ingredient ingredient) {
        try {
            ingredientField.set(recipeIngredient, ingredient);
            return this;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public RecipeIngredientBuilder withQuantity(Quantity quantity) {
        try {
            quantityField.set(recipeIngredient, quantity);
            return this;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public RecipeIngredientBuilder withPrepareMinutes(Integer prepareMinutes) {
        recipeIngredient.setPrepareMinutes(prepareMinutes);
        return this;
    }

    public RecipeIngredientBuilder withInstruction(String instruction) {
        recipeIngredient.setInstruction(instruction);
        return this;
    }

    public RecipeIngredient build() {
        return recipeIngredient;
    }
}
