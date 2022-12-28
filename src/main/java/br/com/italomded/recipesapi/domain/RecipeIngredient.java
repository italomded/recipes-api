package br.com.italomded.recipesapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"ID"})
@Entity(name = "recipe_ingredient")
public class RecipeIngredient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @JoinColumn(nullable = false) @ManyToOne
    private Recipe recipe;
    @JoinColumn(nullable = false) @ManyToOne
    private Ingredient ingredient;

    @Embedded
    private Quantity quantity;
    @Column(nullable = false, length = 1000)
    private String instruction;
    @Column(nullable = false)
    private Integer prepareMinutes;
    @Column(nullable = false)
    private Integer sequence;

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, Quantity quantity, String instruction, Integer prepareMinutes, Integer sequence) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.instruction = instruction;
        this.prepareMinutes = prepareMinutes;
    }
}
