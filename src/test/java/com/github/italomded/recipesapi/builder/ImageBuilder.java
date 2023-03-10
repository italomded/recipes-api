package com.github.italomded.recipesapi.builder;

import com.github.italomded.recipesapi.domain.recipe.Image;
import com.github.italomded.recipesapi.domain.recipe.Recipe;

import java.lang.reflect.Field;

public class ImageBuilder {
    private Image image;

    private Field idField;
    private Field recipeField;

    private ImageBuilder() {
        try {
            idField = Image.class.getDeclaredField("ID");
            idField.setAccessible(true);
            recipeField = Image.class.getDeclaredField("recipe");
            recipeField.setAccessible(true);
            image = new Image();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static ImageBuilder builder() {
        return new ImageBuilder();
    }

    public ImageBuilder withLink(String link) {
        image.setLink(link);
        return this;
    }

    public ImageBuilder withId(Long id) {
        try {
            idField.set(image, id);
            return this;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageBuilder withRecipe(Recipe recipe) {
        try {
            recipeField.set(image, recipe);
            return this;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Image build() {
        return image;
    }
}
