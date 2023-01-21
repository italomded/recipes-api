package com.github.italomded.recipesapi.dto.error;

import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import com.github.italomded.recipesapi.service.exception.DataValidationException;
import org.springframework.validation.FieldError;

public record SimpleErrorResponseDTO(String message) {
    public SimpleErrorResponseDTO(Exception exception) {
        this(exception.getMessage());
    }
}
