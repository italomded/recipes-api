package com.github.italomded.recipesapi.domain.user;

import com.github.italomded.recipesapi.domain.recipe.Recipe;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(of = {"ID"})
@NoArgsConstructor
@Entity(name = "user")
public class ApplicationUser implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Getter
    private Long ID;

    @Column(nullable = false, unique = true, length = 25)
    private String username;
    @Column(nullable = false) @Setter
    private String password;

    @ManyToOne
    @JoinColumn(nullable = false) @Getter @Setter
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_like_recipe",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private Set<Recipe> likedRecipes = new HashSet<>();
    @OneToMany(mappedBy = "creatorUser", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Recipe> recipesCreated  = new HashSet<>();

    public ApplicationUser(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void likeRecipe(Recipe recipe) {
        boolean removed = likedRecipes.remove(recipe);
        if (!removed) likedRecipes.add(recipe);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
