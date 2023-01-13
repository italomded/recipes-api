package com.github.italomded.recipesapi.dto.form;

import com.github.italomded.recipesapi.domain.TypeOfIngredient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IngredientForm(
        @NotBlank
        String name,
        @NotNull
        TypeOfIngredient type
)  {}
