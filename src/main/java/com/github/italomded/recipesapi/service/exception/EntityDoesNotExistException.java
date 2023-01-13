package com.github.italomded.recipesapi.service.exception;

import lombok.Getter;

public class EntityDoesNotExistException extends RuntimeException {
    @Getter
    private Long id;
    @Getter
    private Class<?> exceptionDomainClass;

    public EntityDoesNotExistException(Class<?> exceptionDomainClass, Long id) {
        super("Non-existent id");
        this.id = id;
        this.exceptionDomainClass = exceptionDomainClass;
    }
}
