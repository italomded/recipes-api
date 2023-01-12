package com.github.italomded.recipesapi.service.exception;

import lombok.Getter;

public class EntityDoesNotExistException extends RuntimeException {
    @Getter
    private String label;

    public EntityDoesNotExistException(String message, String label) {
        super(message);
        this.label = label;
    }
}
