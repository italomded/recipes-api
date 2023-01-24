package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.recipe.Measure;
import com.github.italomded.recipesapi.domain.recipe.Quantity;

public record QuantityDTO (double amount, Measure measure) {
    public QuantityDTO(Quantity quantity) {
        this(quantity.getAmount(), quantity.getMeasure());
    }
}
