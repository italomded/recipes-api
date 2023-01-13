package com.github.italomded.recipesapi.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode(of = {"ID"})
@Entity(name = "recipe_ingredient")
public class RecipeIngredient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Getter
    private Long ID;

    @JoinColumn(nullable = false) @ManyToOne @Getter
    private Recipe recipe;
    @JoinColumn(nullable = false) @ManyToOne @Getter
    private Ingredient ingredient;

    @Embedded @Getter
    private Quantity quantity;
    @Column(nullable = false, length = 1000) @Getter @Setter
    private String instruction;
    @Column(nullable = false) @Getter @Setter
    private Integer prepareMinutes;
    @Column(nullable = false) @Getter @Setter
    private Integer sequence;

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, Quantity quantity, String instruction, Integer prepareMinutes, Integer sequence) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.instruction = instruction;
        this.prepareMinutes = prepareMinutes;
        this.sequence = sequence;
    }
}
