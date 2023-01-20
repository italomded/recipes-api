package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.*;
import com.github.italomded.recipesapi.dto.form.*;
import com.github.italomded.recipesapi.repository.ApplicationUserRepository;
import com.github.italomded.recipesapi.repository.IngredientRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Recipe getRecipeById(Long recipeID) {
        return recipeRepository.getReferenceById(recipeID);
    }

    @Transactional
    public Recipe editRecipe(Long recipeID, RecipeEditForm form) {
        // TODO: verify if request user is the author of the recipe
        Recipe recipe = recipeRepository.getReferenceById(recipeID);

        recipe.setTitle(form.title());
        recipe.setDescription(form.description());

        recipeRepository.save(recipe);
        return recipe;
    }

    @Transactional
    public Recipe createRecipe(RecipeCreateForm form) {
        // TODO: set request author user as recipe creator
        ApplicationUser user = applicationUserRepository.findById(1L).get(); // TODO: delete this line

        Recipe recipe = new Recipe(form.title(), form.description());
        for (ImageForm imageForm : form.images()) {
            Image image = new Image(imageForm.bytes(), recipe);
            recipe.addImage(image);
        }

        for (RecipeIngredientCreateWithRecipeForm recipeIngredientForm : form.ingredients()) {
            Ingredient ingredient = ingredientRepository.getReferenceById(recipeIngredientForm.ingredientID());
            Quantity quantity = new Quantity(recipeIngredientForm.amount(), recipeIngredientForm.measure());
            RecipeIngredient recipeIngredient = new RecipeIngredient(
                    recipe, ingredient, quantity, recipeIngredientForm.instruction(),
                    recipeIngredientForm.prepareMinutes()
            );
            ingredient.addRecipeIngredient(recipeIngredient);
            recipe.addRecipeIngredient(recipeIngredient);
        }

        recipe.setCreatorUser(user);
        recipe = recipeRepository.save(recipe);
        return recipe;
    }

    @Transactional
    public boolean deleteRecipe(Long recipeID) {
        // TODO: verify if request user is the author of the recipe
        Recipe recipe = recipeRepository.getReferenceById(recipeID);
        recipe.getUsersThatLiked().forEach(user -> {
            user.likeRecipe(recipe);
            applicationUserRepository.save(user);
        });

        recipeRepository.delete(recipe);
        return true;
    }

    @Transactional
    public void likeRecipe() {
        // TODO: receive a user application to give a like
    }
}
