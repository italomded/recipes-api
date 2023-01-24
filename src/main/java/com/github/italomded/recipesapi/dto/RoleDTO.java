package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.user.Role;

public record RoleDTO (long id, String representation) {
    public RoleDTO(Role role) {
        this(role.getID(), role.getAuthority());
    }
}
