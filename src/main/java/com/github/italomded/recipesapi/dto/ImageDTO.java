package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.recipe.Image;

public record ImageDTO (long id, String link) {
    public ImageDTO(Image image) {
        this(image.getID(), image.getLink());
    }
}
