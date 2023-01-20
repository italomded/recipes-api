package com.github.italomded.recipesapi.service.exception;

import lombok.Getter;

public class BusinessRuleException extends RuntimeException {
    @Getter
    private Class<?> errorClass;

    public BusinessRuleException(Class<?> errorClass, String message) {
        super(message);
        this.errorClass = errorClass;
    }
}
