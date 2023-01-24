package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.recipe.Recipe;

import java.net.URI;

public record RecipeDetailedDTO(long id, long creatorUserId, String title, String description, int likes, URI images, URI ingredients) {
    public RecipeDetailedDTO(Recipe recipe, URI images, URI ingredients) {
        this(
                recipe.getID(),
                recipe.getCreatorUser().getID(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getLikes(),
                images,
                ingredients
                );
    }
}
