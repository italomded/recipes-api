package com.github.italomded.recipesapi.service.exception;

import lombok.Getter;

import java.lang.reflect.Field;

public class DataValidationException extends RuntimeException {
    @Getter
    private Field field;

    public DataValidationException(String message, Field field) {
        super(message);
        this.field = field;
    }
}
