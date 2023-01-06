package com.github.italomded.recipesapi.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode(of = {"ID", "name"})
@Entity(name = "ingredient")
public class Ingredient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false) @OneToMany(mappedBy = "ingredient", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<RecipeIngredient> recipesThatUse;

    @Column(nullable = false, length = 25, unique = true)
    private String name;
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    private TypeOfIngredient category;

    public Ingredient(String name, TypeOfIngredient category) {
        this.recipesThatUse = new HashSet<>();
        this.name = name.toLowerCase();
        this.category = category;
    }
}
