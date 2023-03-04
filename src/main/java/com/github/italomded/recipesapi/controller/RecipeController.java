package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.domain.recipe.Image;
import com.github.italomded.recipesapi.domain.recipe.Recipe;
import com.github.italomded.recipesapi.domain.recipe.RecipeIngredient;
import com.github.italomded.recipesapi.dto.ImageDTO;
import com.github.italomded.recipesapi.dto.RecipeDTO;
import com.github.italomded.recipesapi.dto.RecipeDetailedDTO;
import com.github.italomded.recipesapi.dto.RecipeIngredientDTO;
import com.github.italomded.recipesapi.dto.form.RecipeCreateForm;
import com.github.italomded.recipesapi.dto.form.RecipeEditForm;
import com.github.italomded.recipesapi.service.ImageService;
import com.github.italomded.recipesapi.service.RecipeIngredientService;
import com.github.italomded.recipesapi.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@RestController
@RequestMapping("api/recipes")
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
    public ResponseEntity<Page<RecipeDTO>> getRecipes(
            @PageableDefault(sort = {"usersWhoLiked"}) Pageable pageable, @RequestParam(required = false) Long[] ingredients) {
        Page<Recipe> recipes;
        if (ingredients == null) {
            recipes = recipeService.getAllRecipes(pageable);
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
            recipes = recipeService.getRecipesByIngredients(pageable, ingredients);
        }
        Page<RecipeDTO> recipesDTO = recipes.map(RecipeDTO::new);
        return ResponseEntity.ok(recipesDTO);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<Page<RecipeDTO>> getAllRecipesOfAUser(@PathVariable long userId, Pageable pageable) {
        Page<Recipe> recipes = recipeService.getRecipesByUserId(pageable, userId);
        Page<RecipeDTO> recipesDTO = recipes.map(RecipeDTO::new);
        return ResponseEntity.ok(recipesDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<RecipeDetailedDTO> getRecipe(@PathVariable long id, UriComponentsBuilder uriComponentsBuilder) {
        Recipe recipe = recipeService.getRecipeById(id);
        URI recipeImagesURI = uriComponentsBuilder.path("/api/recipes/images/{id}").buildAndExpand(recipe.getID()).toUri();
        URI recipeIngredientsURI = uriComponentsBuilder.replacePath("/api/recipes/ingredients/{id}").buildAndExpand(recipe.getID()).toUri();
        return ResponseEntity.ok(new RecipeDetailedDTO(recipe, recipeImagesURI, recipeIngredientsURI));
    }

    @GetMapping("ingredients/{id}")
    public ResponseEntity<Page<RecipeIngredientDTO>> getIngredientsOfRecipe(@PathVariable long id, @PageableDefault(sort = {"sequence"}) Pageable pageable) {
        Page<RecipeIngredient> recipeIngredients = recipeIngredientService.getRecipeIngredientsByRecipeId(pageable, id);
        Page<RecipeIngredientDTO> recipeIngredientDTO = recipeIngredients.map(RecipeIngredientDTO::new);
        return ResponseEntity.ok(recipeIngredientDTO);
    }

    @GetMapping("images/{id}")
    public ResponseEntity<Page<ImageDTO>> getImagesOfRecipe(@PathVariable long id, Pageable pageable) {
        Page<Image> images = imageService.getImagesByRecipeId(pageable, id);
        Page<ImageDTO> imagesDTO = images.map(ImageDTO::new);
        return ResponseEntity.ok(imagesDTO);
    }

    @PutMapping("{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<RecipeDTO> editRecipe(@PathVariable long id, @RequestBody @Valid RecipeEditForm form) {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Recipe recipe = recipeService.editRecipe(id, form, user);
        return ResponseEntity.ok(new RecipeDTO(recipe));
    }

    @PatchMapping("{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity likeRecipe(@PathVariable long id) {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipeService.likeRecipe(id, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody @Valid RecipeCreateForm form, UriComponentsBuilder uriComponentsBuilder) {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Recipe recipe = recipeService.createRecipe(form, user);
        URI location = uriComponentsBuilder.path("/api/recipes/{id}").buildAndExpand(recipe.getID()).toUri();
        return ResponseEntity.created(location).body(new RecipeDTO(recipe));
    }

    @DeleteMapping("{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity deleteRecipe(@PathVariable long id) {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipeService.deleteRecipe(id, user);
        return ResponseEntity.noContent().build();
    }
}
