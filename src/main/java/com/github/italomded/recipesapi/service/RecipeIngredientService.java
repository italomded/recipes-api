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
import com.github.italomded.recipesapi.service.exception.EntityDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Page<RecipeService> getRecipeIngredientsByRecipeId(Pageable pageable, Long recipeID) {
        return recipeIngredientRepository.findByRecipe_ID(recipeID, pageable);
    }

    public Long editRecipeIngredient(Long recipeIngredientID, RecipeIngredientEditForm form) {
        // TODO: verify if request user is the author of the recipe
        Optional<RecipeIngredient> optionalRecipeIngredient = recipeIngredientRepository.findById(recipeIngredientID);
        if (optionalRecipeIngredient.isEmpty()) {
            throw new EntityDoesNotExistException("Non-existent recipe ingredient id", recipeIngredientID.getClass().getName());
        }

        RecipeIngredient recipeIngredient = optionalRecipeIngredient.get();
        recipeIngredient.setInstruction(form.instruction());
        recipeIngredient.setPrepareMinutes(form.prepareMinutes());
        recipeIngredient.setPrepareMinutes(form.prepareMinutes());
        recipeIngredient.getQuantity().setAmount(form.amount());
        recipeIngredient.getQuantity().setMeasure(form.measure());
        recipeIngredientRepository.save(recipeIngredient);

        return recipeIngredient.getID();
    }

    public Long createRecipeIngredient(Long recipeID, RecipeIngredientCreateForm form) {
        // TODO: verify if request user is the author of the recipe
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeID);
        if (optionalRecipe.isEmpty()) {
            throw new EntityDoesNotExistException("Non-existent recipe id", recipeID.getClass().getName());
        }
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(form.ingredientID());
        if (optionalIngredient.isEmpty()) {
            throw new EntityDoesNotExistException("Non-existent ingredient id", form.ingredientID().getClass().getName());
        }

        Recipe recipe = optionalRecipe.get();
        Quantity quantity = new Quantity(form.amount(), form.measure());
        RecipeIngredient recipeIngredient = new RecipeIngredient(
                recipe, optionalIngredient.get(), quantity, form.instruction(), form.prepareMinutes(), form.sequence()
                );

        recipe.addRecipeIngredient(recipeIngredient);

        recipeIngredient = recipeIngredientRepository.save(recipeIngredient);
        recipeRepository.save(recipe);

        return recipeIngredient.getID();
    }

    public void deleteRecipeIngredient(Long recipeIngredientID) {
        // TODO: verify if request user is the author of the recipe
        Optional<RecipeIngredient> optionalRecipeIngredient = recipeIngredientRepository.findById(recipeIngredientID);
        if (optionalRecipeIngredient.isEmpty()) {
            throw new EntityDoesNotExistException("Non-existent recipe ingredient id", recipeIngredientID.getClass().getName());
        }

        RecipeIngredient recipeIngredient = optionalRecipeIngredient.get();
        Recipe recipe = recipeIngredient.getRecipe();
        if (recipe.getIngredientsOfRecipe().size() == 3) {
            throw new BusinessRuleException("Can't delete recipe ingredient because the recipe ingredient list should have at least 3 ingredients");
        }

        recipe.removeRecipeIngredient(recipeIngredient);
        recipeIngredientRepository.delete(recipeIngredient);
        recipeRepository.save(recipe);
    }
}
