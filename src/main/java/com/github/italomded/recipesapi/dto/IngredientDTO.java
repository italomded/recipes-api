package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.Ingredient;
import com.github.italomded.recipesapi.domain.TypeOfIngredient;

public record IngredientDTO (long id, String name, TypeOfIngredient category) {
    public IngredientDTO(Ingredient ingredient) {
        this(ingredient.getID(), ingredient.getName(), ingredient.getCategory());
    }
}
