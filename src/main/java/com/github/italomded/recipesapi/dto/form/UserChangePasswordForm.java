package com.github.italomded.recipesapi.dto.form;

import jakarta.validation.constraints.NotBlank;

public record UserChangePasswordForm(
        @NotBlank
        String oldPassword,
        @NotBlank
        String newPassword
) { }
