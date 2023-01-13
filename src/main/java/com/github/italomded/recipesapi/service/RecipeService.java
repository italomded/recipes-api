package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.*;
import com.github.italomded.recipesapi.dto.form.ImageForm;
import com.github.italomded.recipesapi.dto.form.RecipeCreateForm;
import com.github.italomded.recipesapi.dto.form.RecipeEditForm;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientCreateForm;
import com.github.italomded.recipesapi.repository.ApplicationUserRepository;
import com.github.italomded.recipesapi.repository.IngredientRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import com.github.italomded.recipesapi.service.exception.EntityDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, ApplicationUserRepository applicationUserRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    public Page<Recipe> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    public Page<Recipe> getRecipesByUserId(Pageable pageable, Long userID) {
        return recipeRepository.findByCreatorUser_ID(userID, pageable);
    }

    public Optional<Recipe> getRecipeById(Long recipeID) {
        return recipeRepository.findById(recipeID);
    }

    public Long editRecipe(Long recipeID, RecipeEditForm form) {
        // TODO: verify if request user is the author of the recipe
        Optional<Recipe> optionalRecipe = getRecipeById(recipeID);
        if (optionalRecipe.isEmpty()) {
            throw new EntityDoesNotExistException("Non-existent ingredient id", recipeID.getClass().getName());
        }

        Recipe recipe = optionalRecipe.get();
        recipe.setTitle(form.title());
        recipe.setDescription(form.description());
        recipeRepository.save(recipe);
        return recipe.getID();
    }

    public Long createRecipe(RecipeCreateForm form) {
        // TODO: set request author user as recipe creator
        ApplicationUser user = applicationUserRepository.findById(1L).get(); // TODO: delete this line

        Recipe recipe = new Recipe(form.title(), form.description());
        for (ImageForm imageForm : form.images()) {
            Image image = new Image(imageForm.bytes(), recipe);
            recipe.addImage(image);
        }

        for (RecipeIngredientCreateForm recipeIngredientForm : form.ingredients()) {
            Optional<Ingredient> optionalIngredient = ingredientRepository.findById(recipeIngredientForm.ingredientID());
            if (optionalIngredient.isEmpty()) {
                throw new EntityDoesNotExistException("Non-existent ingredient id", recipeIngredientForm.ingredientID().getClass().getName());
            }
            Quantity quantity = new Quantity(recipeIngredientForm.amount(), recipeIngredientForm.measure());
            RecipeIngredient recipeIngredient = new RecipeIngredient(
                    recipe, optionalIngredient.get(), quantity, recipeIngredientForm.instruction(),
                    recipeIngredientForm.prepareMinutes(), recipeIngredientForm.sequence()
            );
            recipe.addRecipeIngredient(recipeIngredient);
        }

        recipe.setCreatorUser(user);
        recipe = recipeRepository.save(recipe);
        return recipe.getID();
    }

    public void deleteRecipe(Long recipeID) {
        // TODO: verify if request user is the author of the recipe
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeID);
        if (optionalRecipe.isEmpty()) {
            throw new EntityDoesNotExistException("Non-existent recipe id", recipeID.getClass().getName());
        }
        Recipe recipe = optionalRecipe.get();
        recipeRepository.delete(recipe);
    }
}
