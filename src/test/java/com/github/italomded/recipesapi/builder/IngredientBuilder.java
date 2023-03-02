package com.github.italomded.recipesapi.builder;

import com.github.italomded.recipesapi.domain.recipe.Ingredient;
import com.github.italomded.recipesapi.domain.recipe.TypeOfIngredient;

import java.lang.reflect.Field;

public class IngredientBuilder {
    private Ingredient ingredient;
    private Field idField;

    private IngredientBuilder() {
        try {
            idField = Ingredient.class.getDeclaredField("ID");
            idField.setAccessible(true);
            ingredient = new Ingredient();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static IngredientBuilder builder() {
        return new IngredientBuilder();
    }

    public IngredientBuilder withId(Long id) {
        try {
            idField.set(ingredient, id);
            return this;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public IngredientBuilder withCategory(TypeOfIngredient category) {
        ingredient.setCategory(category);
        return this;
    }

    public IngredientBuilder withName(String name) {
        ingredient.setName(name);
        return this;
    }

    public Ingredient build() {
        return ingredient;
    }
}
