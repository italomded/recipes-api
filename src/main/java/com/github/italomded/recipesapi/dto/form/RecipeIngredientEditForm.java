package com.github.italomded.recipesapi.dto.form;

import com.github.italomded.recipesapi.domain.Measure;
import jakarta.validation.constraints.NotNull;

public record RecipeIngredientEditForm(
        @NotNull
        Double amount,
        @NotNull
        Measure measure,
        @NotNull
        String instruction,
        @NotNull
        Integer prepareMinutes
) { }