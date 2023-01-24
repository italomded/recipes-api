package com.github.italomded.recipesapi.dto.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecipeCreateForm (
        @Size(min = 1)
        ImageForm[] images,
        @NotBlank
        String title,
        @NotBlank
        String description,
        @Size(min = 3) // TODO: annotation to verify sequence order
        RecipeIngredientCreateWithRecipeForm[] ingredients
) { }
