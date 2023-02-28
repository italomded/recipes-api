package com.github.italomded.recipesapi.dto.form;

import com.github.italomded.recipesapi.domain.recipe.Measure;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecipeIngredientCreateWithSequenceForm extends RecipeIngredientCreateForm {
    @NotNull
    private Integer sequence;

    public RecipeIngredientCreateWithSequenceForm(Double amount, Measure measure, String instruction, Integer prepareMinutes, Long ingredientID, Integer sequence) {
        super(amount, measure, instruction, prepareMinutes, ingredientID);
        this.sequence = sequence;
    }
}
