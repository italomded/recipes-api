package com.github.italomded.recipesapi.dto;

import com.github.italomded.recipesapi.domain.Measure;
import com.github.italomded.recipesapi.domain.Quantity;

public record QuantityDTO (double amount, Measure measure) {
    public QuantityDTO(Quantity quantity) {
        this(quantity.getAmount(), quantity.getMeasure());
    }
}
