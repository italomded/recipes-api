package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.recipe.Ingredient;
import com.github.italomded.recipesapi.domain.recipe.Quantity;
import com.github.italomded.recipesapi.domain.recipe.Recipe;
import com.github.italomded.recipesapi.domain.recipe.RecipeIngredient;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientCreateWithSequenceForm;
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
    public RecipeIngredient editRecipeIngredient(Long recipeIngredientID, RecipeIngredientEditForm form, ApplicationUser userAuthor) {
        RecipeIngredient recipeIngredient = recipeIngredientRepository.getReferenceById(recipeIngredientID);
        RecipeService.verifyIfIsTheAuthor(recipeIngredient.getRecipe(), userAuthor);

        recipeIngredient.setInstruction(form.getInstruction());
        recipeIngredient.setPrepareMinutes(form.getPrepareMinutes());
        recipeIngredient.setPrepareMinutes(form.getPrepareMinutes());
        recipeIngredient.getQuantity().setAmount(form.getAmount());
        recipeIngredient.getQuantity().setMeasure(form.getMeasure());

        recipeIngredientRepository.save(recipeIngredient);
        return recipeIngredient;
    }

    @Transactional
    public RecipeIngredient createRecipeIngredient(Long recipeID, RecipeIngredientCreateWithSequenceForm form, ApplicationUser userAuthor) {
        Recipe recipe = recipeRepository.getReferenceById(recipeID);
        RecipeService.verifyIfIsTheAuthor(recipe, userAuthor);

        Ingredient ingredient = ingredientRepository.getReferenceById(form.getIngredientID());

        Quantity quantity = new Quantity(form.getAmount(), form.getMeasure());
        RecipeIngredient recipeIngredient = new RecipeIngredient(
                recipe, ingredient, quantity, form.getInstruction(), form.getPrepareMinutes()
                );

        ingredient.addRecipeIngredient(recipeIngredient);
        recipe.addRecipeIngredient(recipeIngredient, form.getSequence());

        recipeIngredient = recipeIngredientRepository.save(recipeIngredient);
        recipeRepository.save(recipe);

        return recipeIngredient;
    }

    @Transactional
    public boolean deleteRecipeIngredient(Long recipeIngredientID, ApplicationUser userAuthor) {
        RecipeIngredient recipeIngredient = recipeIngredientRepository.getReferenceById(recipeIngredientID);
        Recipe recipe = recipeIngredient.getRecipe();
        RecipeService.verifyIfIsTheAuthor(recipe, userAuthor);

        boolean removed = recipe.removeRecipeIngredient(recipeIngredient);
        if (removed) {
            recipeIngredientRepository.delete(recipeIngredient);
            recipeRepository.save(recipe);
            return true;
        } else {
            throw new BusinessRuleException(
                    Recipe.class,
                    "can't delete recipe ingredient because the recipe ingredient list should have at least 3 ingredients or the ingredient does not belong in the recipe");
        }
    }
}
