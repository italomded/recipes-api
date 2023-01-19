package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.Image;

public record ImageDTO (long id, byte[] bytes) {
    public ImageDTO(Image image) {
        this(image.getID(), image.getImageBytes());
    }
}
