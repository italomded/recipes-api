package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.domain.Image;
import com.github.italomded.recipesapi.domain.Recipe;
import com.github.italomded.recipesapi.domain.RecipeIngredient;
import com.github.italomded.recipesapi.dto.ImageDTO;
import com.github.italomded.recipesapi.dto.RecipeDTO;
import com.github.italomded.recipesapi.dto.RecipeIngredientDTO;
import com.github.italomded.recipesapi.dto.form.RecipeCreateForm;
import com.github.italomded.recipesapi.dto.form.RecipeEditForm;
import com.github.italomded.recipesapi.service.ImageService;
import com.github.italomded.recipesapi.service.RecipeIngredientService;
import com.github.italomded.recipesapi.service.RecipeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("api/recipe")
public class RecipeController {
    private RecipeService recipeService;
    private RecipeIngredientService recipeIngredientService;
    private ImageService imageService;

    @Autowired
    public RecipeController(RecipeService recipeService, RecipeIngredientService recipeIngredientService, ImageService imageService) {
        this.recipeService = recipeService;
        this.recipeIngredientService = recipeIngredientService;
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<Page<RecipeDTO>> getAllRecipes(Pageable pageable) {
        Page<Recipe> allRecipes = recipeService.getAllRecipes(pageable);
        Page<RecipeDTO> allRecipesDTO = allRecipes.map(RecipeDTO::new);
        return ResponseEntity.ok(allRecipesDTO);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<Page<RecipeDTO>> getAllRecipesOfAUser(@PathVariable long userId, Pageable pageable) {
        Page<Recipe> recipes = recipeService.getRecipesByUserId(pageable, userId);
        Page<RecipeDTO> recipesDTO = recipes.map(RecipeDTO::new);
        return ResponseEntity.ok(recipesDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable long id) {
        Optional<Recipe> optionalRecipe = recipeService.getRecipeById(id);
        if (optionalRecipe.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new RecipeDTO(optionalRecipe.get()));
    }

    @GetMapping("ingredient/{id}")
    public ResponseEntity<Page<RecipeIngredientDTO>> getIngredientsOfRecipe(@PathVariable long id, @PageableDefault(sort = {"sequence"}) Pageable pageable) {
        Page<RecipeIngredient> recipeIngredients = recipeIngredientService.getRecipeIngredientsByRecipeId(pageable, id);
        Page<RecipeIngredientDTO> recipeIngredientDTO = recipeIngredients.map(RecipeIngredientDTO::new);
        return ResponseEntity.ok(recipeIngredientDTO);
    }

    @GetMapping("image/{id}")
    public ResponseEntity<Page<ImageDTO>> getImagesOfRecipe(@PathVariable long id, Pageable pageable) {
        Page<Image> images = imageService.getImagesByRecipeId(pageable, id);
        Page<ImageDTO> imagesDTO = images.map(ImageDTO::new);
        return ResponseEntity.ok(imagesDTO);
    }

    @Transactional
    @PutMapping("{id}")
    public ResponseEntity<RecipeDTO> editRecipe(@PathVariable long id, @RequestBody @Valid RecipeEditForm form) {
        Recipe recipe = recipeService.editRecipe(id, form);
        return ResponseEntity.ok(new RecipeDTO(recipe));
    }

    @Transactional
    @PatchMapping
    public ResponseEntity likeRecipe() {
        // TODO: change
        recipeService.likeRecipe();
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody @Valid RecipeCreateForm form, UriComponentsBuilder uriComponentsBuilder) {
        Recipe recipe = recipeService.createRecipe(form);
        URI location = uriComponentsBuilder.path("/api/recipe/{id}").buildAndExpand(recipe.getID()).toUri();
        return ResponseEntity.created(location).body(new RecipeDTO(recipe));
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity deleteRecipe(@PathVariable long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
