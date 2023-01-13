package com.github.italomded.recipesapi.service.exception;

import lombok.Getter;

import java.lang.reflect.Field;

public class DataValidationException extends RuntimeException {
    @Getter
    private Field label;

    public DataValidationException(String message, Field label) {
        super(message);
        this.label = label;
    }
}
