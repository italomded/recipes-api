package com.github.italomded.recipesapi.dto.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecipeCreateForm (
        @Size(min = 1, max = 5) @Valid
        ImageForm[] images,
        @NotBlank
        String title,
        @NotBlank
        String description,
        @Size(min = 3, max = 50) @Valid
        RecipeIngredientCreateWithRecipeForm[] ingredients
) { }
