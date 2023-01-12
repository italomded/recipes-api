package com.github.italomded.recipesapi.dto.form;


import jakarta.validation.constraints.NotBlank;

public record RecipeEditForm(
        @NotBlank String title,
        @NotBlank String description
) {}
