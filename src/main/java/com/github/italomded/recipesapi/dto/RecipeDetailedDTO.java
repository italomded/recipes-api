package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.recipe.Recipe;
import lombok.Getter;

import java.net.URI;

@Getter
public class RecipeDetailedDTO extends RecipeDTO {
    private URI images;
    private URI ingredients;

    public RecipeDetailedDTO(Recipe recipe, URI images, URI ingredients) {
        super(recipe);
        this.images = images;
        this.ingredients = ingredients;
    }
}
