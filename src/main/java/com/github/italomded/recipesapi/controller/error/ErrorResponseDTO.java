package com.github.italomded.recipesapi.controller.error;

import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import com.github.italomded.recipesapi.service.exception.DataValidationException;
import org.springframework.validation.FieldError;
public record ErrorResponseDTO(String label, String message) {
    public ErrorResponseDTO(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }

    public ErrorResponseDTO(DataValidationException exception) {
        this(exception.getField().getName(), exception.getMessage());
    }

    public ErrorResponseDTO(BusinessRuleException exception) {
        this(
                exception.getErrorClass().getSimpleName().toLowerCase(),
                exception.getMessage()
        );
    }
}
