package com.github.italomded.recipesapi.domain.user;

import com.github.italomded.recipesapi.domain.recipe.Recipe;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode(of = {"ID"})
@Entity(name = "role")
public class Role implements GrantedAuthority {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Getter
    private Long ID;

    @Column(nullable = false, unique = true)
    private String representation;

    @OneToMany(mappedBy = "role", orphanRemoval = true)
    private Set<ApplicationUser> users = new HashSet<>();

    public Role(String representation) {
        this.representation = representation;
    }

    public void removeUser(ApplicationUser user) {
        users.remove(user);
    }

    public void addUser(ApplicationUser user) {
        users.add(user);
    }

    public Set<ApplicationUser> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    @Override
    public String getAuthority() {
        return representation;
    }
}
