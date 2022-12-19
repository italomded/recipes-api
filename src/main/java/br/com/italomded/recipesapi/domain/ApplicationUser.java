package br.com.italomded.recipesapi.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(of = {"ID"})
@NoArgsConstructor
@Entity(name = "user")
public class ApplicationUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_like_recipe",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private Set<Recipe> likedRecipes;
    @OneToMany(mappedBy = "creatorUser", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Recipe> recipesCreated;
}
