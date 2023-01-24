package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;

public record ApplicationUserDTO (long id, String username) {
    public ApplicationUserDTO(ApplicationUser applicationUser) {
        this(applicationUser.getID(), applicationUser.getUsername());
    }
}
