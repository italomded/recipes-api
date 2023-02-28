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
public class RecipeIngredientCreateForm extends RecipeIngredientEditForm {
        @NotNull
        private Long ingredientID;

        public RecipeIngredientCreateForm(Double amount, Measure measure, String instruction, Integer prepareMinutes, Long ingredientID) {
                super(amount, measure, instruction, prepareMinutes);
                this.ingredientID = ingredientID;
        }
}
