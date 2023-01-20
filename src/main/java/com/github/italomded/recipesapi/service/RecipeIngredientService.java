package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.Ingredient;
import com.github.italomded.recipesapi.domain.Quantity;
import com.github.italomded.recipesapi.domain.Recipe;
import com.github.italomded.recipesapi.domain.RecipeIngredient;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientCreateForm;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientEditForm;
import com.github.italomded.recipesapi.repository.IngredientRepository;
import com.github.italomded.recipesapi.repository.RecipeIngredientRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RecipeIngredientService {
    private RecipeIngredientRepository recipeIngredientRepository;
    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;

    @Autowired
    public RecipeIngredientService(RecipeIngredientRepository recipeIngredientRepository, RecipeRepository recipeRepository,
                                   IngredientRepository ingredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public Page<RecipeIngredient> getRecipeIngredientsByRecipeId(Pageable pageable, Long recipeID) {
        return recipeIngredientRepository.findByRecipe_ID(recipeID, pageable);
    }

    @Transactional
    public RecipeIngredient editRecipeIngredient(Long recipeIngredientID, RecipeIngredientEditForm form) {
        // TODO: verify if request user is the author of the recipe
        RecipeIngredient recipeIngredient = recipeIngredientRepository.getReferenceById(recipeIngredientID);

        recipeIngredient.setInstruction(form.instruction());
        recipeIngredient.setPrepareMinutes(form.prepareMinutes());
        recipeIngredient.setPrepareMinutes(form.prepareMinutes());
        recipeIngredient.getQuantity().setAmount(form.amount());
        recipeIngredient.getQuantity().setMeasure(form.measure());

        recipeIngredientRepository.save(recipeIngredient);
        return recipeIngredient;
    }

    @Transactional
    public RecipeIngredient createRecipeIngredient(Long recipeID, RecipeIngredientCreateForm form) {
        // TODO: verify if request user is the author of the recipe
        Recipe recipe = recipeRepository.getReferenceById(recipeID);
        Ingredient ingredient = ingredientRepository.getReferenceById(form.ingredientID());

        Quantity quantity = new Quantity(form.amount(), form.measure());
        RecipeIngredient recipeIngredient = new RecipeIngredient(
                recipe, ingredient, quantity, form.instruction(), form.prepareMinutes(), form.sequence()
                );
        ingredient.addRecipeIngredient(recipeIngredient);
        recipe.addRecipeIngredient(recipeIngredient);

        recipeIngredient = recipeIngredientRepository.save(recipeIngredient);
        recipeRepository.save(recipe);

        return recipeIngredient;
    }

    @Transactional
    public boolean deleteRecipeIngredient(Long recipeIngredientID) {
        // TODO: verify if request user is the author of the recipe
        RecipeIngredient recipeIngredient = recipeIngredientRepository.getReferenceById(recipeIngredientID);
        Recipe recipe = recipeIngredient.getRecipe();

        if (recipe.getIngredientsOfRecipe().size() < 4) {
            throw new BusinessRuleException(Recipe.class, "can't delete recipe ingredient because the recipe ingredient list should have at least 3 ingredients");
        }

        recipe.removeRecipeIngredient(recipeIngredient);
        recipeIngredientRepository.delete(recipeIngredient);
        recipeRepository.save(recipe);
        return true;
    }
}
