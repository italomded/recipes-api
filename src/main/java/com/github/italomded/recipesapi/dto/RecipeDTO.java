package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.recipe.Recipe;
import lombok.Getter;

@Getter
public class RecipeDTO {
    private long id;
    private long creatorUserId;
    private String title;
    private String description;
    private int likes;

    public RecipeDTO(Recipe recipe) {
        this.id = recipe.getID();
        this.creatorUserId = recipe.getCreatorUser().getID();
        this.title = recipe.getTitle();
        this.description = recipe.getDescription();
        this.likes = recipe.getLikes();
    }
}
