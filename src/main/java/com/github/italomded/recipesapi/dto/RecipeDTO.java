package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.recipe.Recipe;

public record RecipeDTO (long id, long creatorUserId, String title, String description, int likes) {
    public RecipeDTO(Recipe recipe) {
        this(
                recipe.getID(),
                recipe.getCreatorUser().getID(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getLikes()
                );
    }
}
