package com.github.italomded.recipesapi.dto.form;

import com.github.italomded.recipesapi.domain.recipe.Measure;
import jakarta.validation.constraints.NotNull;

public record RecipeIngredientCreateForm(
        @NotNull
        Long ingredientID,
        @NotNull
        Double amount,
        @NotNull
        Measure measure,
        @NotNull
        String instruction,
        @NotNull
        Integer prepareMinutes,
        @NotNull
        Integer sequence
) { }
