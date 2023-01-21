package com.github.italomded.recipesapi.security.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(Exception exception) {
        super(exception);
    }
}
