package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserDTO {
    private long id;
    private String username;

    public ApplicationUserDTO(ApplicationUser applicationUser) {
        this(applicationUser.getID(), applicationUser.getUsername());
    }
}