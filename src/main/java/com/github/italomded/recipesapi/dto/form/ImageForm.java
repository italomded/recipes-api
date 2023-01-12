package com.github.italomded.recipesapi.dto.form;

import jakarta.validation.constraints.NotNull;

public record ImageForm(
        @NotNull
        byte[] bytes
) {}
