package com.github.italomded.recipesapi.dto.form;

import com.github.italomded.recipesapi.domain.recipe.Measure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecipeIngredientCreateWithRecipeForm(
        @NotNull
        Long ingredientID,
        @NotNull
        Double amount,
        @NotNull
        Measure measure,
        @NotBlank
        String instruction,
        @NotNull
        Integer prepareMinutes
) { }
