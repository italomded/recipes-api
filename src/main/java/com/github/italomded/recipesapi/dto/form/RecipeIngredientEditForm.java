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
@AllArgsConstructor
public class RecipeIngredientEditForm {
        @NotNull
        private Double amount;
        @NotNull
        private Measure measure;
        @NotNull
        private String instruction;
        @NotNull
        private Integer prepareMinutes;
}
