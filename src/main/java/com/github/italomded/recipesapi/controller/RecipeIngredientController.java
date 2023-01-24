package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.domain.recipe.RecipeIngredient;
import com.github.italomded.recipesapi.dto.RecipeIngredientDTO;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientCreateForm;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientEditForm;
import com.github.italomded.recipesapi.service.RecipeIngredientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/recipe-ingredient")
public class RecipeIngredientController {
    private RecipeIngredientService recipeIngredientService;

    @Autowired
    public RecipeIngredientController(RecipeIngredientService recipeIngredientService) {
        this.recipeIngredientService = recipeIngredientService;
    }

    @PutMapping("{id}")
    public ResponseEntity<RecipeIngredientDTO> editRecipeIngredient(@PathVariable long id, @RequestBody @Valid RecipeIngredientEditForm form) {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RecipeIngredient recipeIngredient = recipeIngredientService.editRecipeIngredient(id, form, user);

        return ResponseEntity.ok(new RecipeIngredientDTO(recipeIngredient));
    }

    @PostMapping("{recipeId}")
    public ResponseEntity<RecipeIngredientDTO> createRecipeIngredient(@PathVariable long recipeId, @RequestBody @Valid RecipeIngredientCreateForm form,
                                                                      UriComponentsBuilder uriComponentsBuilder) {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RecipeIngredient recipeIngredient = recipeIngredientService.createRecipeIngredient(recipeId, form, user);

        URI location = uriComponentsBuilder.path("/api/recipe/{id}").buildAndExpand(recipeIngredient.getRecipe().getID()).toUri();
        return ResponseEntity.created(location).body(new RecipeIngredientDTO(recipeIngredient));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteRecipeIngredient(@PathVariable long id) {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipeIngredientService.deleteRecipeIngredient(id, user);

        return ResponseEntity.noContent().build();
    }

}
