package com.github.italomded.recipesapi.domain.recipe;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode(of = {"ID", "name"})
@Entity(name = "ingredient")
public class Ingredient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Getter
    private Long ID;

    @Column(nullable = false) @OneToMany(mappedBy = "ingredient", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<RecipeIngredient> recipesThatUse = new HashSet<>();

    @Column(nullable = false, length = 25, unique = true) @Getter @Setter
    private String name;
    @Column(nullable = false) @Enumerated(EnumType.STRING) @Getter @Setter
    private TypeOfIngredient category;

    public Ingredient(String name, TypeOfIngredient category) {
        this.name = name.toLowerCase();
        this.category = category;
    }

    public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipesThatUse.add(recipeIngredient);
    }
}
