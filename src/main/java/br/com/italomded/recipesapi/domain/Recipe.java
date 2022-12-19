package br.com.italomded.recipesapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"ID"})
@Entity(name = "recipe")
public class Recipe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false) @ManyToOne
    private ApplicationUser creatorUser;

    @Column(nullable = false)
    private Set<byte[]> images;
    @Column(nullable = false, length = 50)
    private String title;
    @Column(length = 500)
    private String description;

    @Column(nullable = false) @OneToMany(mappedBy = "recipe", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecipeIngredient> ingredientsOfRecipe;
    @Column(nullable = false) @ManyToMany(mappedBy = "likedRecipes", fetch = FetchType.LAZY)
    private Set<ApplicationUser> usersWhoLiked;

    public Recipe(String title, String description) {
        this.images = new HashSet<>();
        this.title = title;
        this.description = description;
        this.ingredientsOfRecipe = new ArrayList<>();
        this.usersWhoLiked = new HashSet<>();
    }
}
