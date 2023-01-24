package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;

import java.net.URI;

public record ApplicationUserDetailedDTO(long id, String username, RoleDTO role, URI recipes) {
    public ApplicationUserDetailedDTO(ApplicationUser applicationUser, URI recipes) {
        this(applicationUser.getID(), applicationUser.getUsername(), new RoleDTO(applicationUser.getRole()), recipes);
    }
}
