package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.RecipeIngredient;

public record RecipeIngredientDTO (long id, IngredientDTO ingredient, QuantityDTO quantity, String instruction, int prepareMinutes, int sequence) {
    public RecipeIngredientDTO(RecipeIngredient recipeIngredient) {
        this(
                recipeIngredient.getID(),
                new IngredientDTO(recipeIngredient.getIngredient()),
                new QuantityDTO((recipeIngredient.getQuantity())),
                recipeIngredient.getInstruction(),
                recipeIngredient.getPrepareMinutes(),
                recipeIngredient.getSequence()
        );
    }
}
