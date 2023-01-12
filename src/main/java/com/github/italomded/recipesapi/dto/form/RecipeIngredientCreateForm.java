package com.github.italomded.recipesapi.dto.form;

import com.github.italomded.recipesapi.domain.Ingredient;
import com.github.italomded.recipesapi.domain.Measure;
import com.github.italomded.recipesapi.domain.Quantity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
