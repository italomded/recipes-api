package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationUserDetailedDTO extends ApplicationUserDTO {
    private RoleDTO role;
    private URI recipes;

    public ApplicationUserDetailedDTO(ApplicationUser applicationUser, URI recipes) {
        super(applicationUser);
        this.role = new RoleDTO(applicationUser.getRole());
        this.recipes = recipes;
    }

    public ApplicationUserDetailedDTO(ApplicationUser applicationUser, RoleDTO role, URI recipes) {
        super(applicationUser);
        this.role = role;
        this.recipes = recipes;
    }
}
