package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.recipe.*;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.dto.form.*;
import com.github.italomded.recipesapi.repository.*;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    private RecipeIngredientRepository recipeIngredientRepository;
    private ImageRepository imageRepository;
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository,
                         RecipeIngredientRepository recipeIngredientRepository, ImageRepository imageRepository,
                         ApplicationUserRepository applicationUserRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.imageRepository = imageRepository;
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
    public Recipe editRecipe(Long recipeID, RecipeEditForm form, ApplicationUser userAuthor) {
        Recipe recipe = recipeRepository.getReferenceById(recipeID);
        this.verifyIfIsTheAuthor(recipe, userAuthor);

        recipe.setTitle(form.title());
        recipe.setDescription(form.description());

        recipeRepository.save(recipe);
        return recipe;
    }

    @Transactional
    public Recipe createRecipe(RecipeCreateForm form, ApplicationUser userAuthor) {
        Recipe recipe = new Recipe(form.title(), form.description(), userAuthor);
        recipe = recipeRepository.save(recipe);

        for (ImageForm imageForm : form.images()) {
            Image image = new Image(imageForm.link(), recipe);
            recipe.addImage(image);
            imageRepository.save(image);
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
            recipeIngredientRepository.save(recipeIngredient);
        }

        recipe = recipeRepository.save(recipe);
        return recipe;
    }

    @Transactional
    public boolean deleteRecipe(Long recipeID, ApplicationUser userAuthor) {
        Recipe recipe = recipeRepository.getReferenceById(recipeID);
        this.verifyIfIsTheAuthor(recipe, userAuthor);
        recipe.getUsersThatLiked().forEach(user -> {
            user.likeRecipe(recipe);
            applicationUserRepository.save(user);
        });

        recipeRepository.delete(recipe);
        return true;
    }

    @Transactional
    public void likeRecipe(Long recipeId, ApplicationUser user) {
        Recipe recipe = recipeRepository.getReferenceById(recipeId);
        recipe.likeRecipe(user);
    }

    public static boolean verifyIfIsTheAuthor(Recipe recipe, ApplicationUser user) {
        if (recipe.getCreatorUser().equals(user)) return true;
        throw new BusinessRuleException(Recipe.class, "authenticated user is not the author of the recipe");
    }
}
