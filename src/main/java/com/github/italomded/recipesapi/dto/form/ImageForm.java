package com.github.italomded.recipesapi.dto.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ImageForm(
        @NotBlank
        String link
) {}
