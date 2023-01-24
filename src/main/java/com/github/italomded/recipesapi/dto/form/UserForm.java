package com.github.italomded.recipesapi.dto.form;

import jakarta.validation.constraints.NotBlank;

public record UserForm(
        @NotBlank
        String username,
        @NotBlank
        String password
) { }
