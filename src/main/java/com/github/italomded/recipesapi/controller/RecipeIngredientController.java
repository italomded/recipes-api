package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.domain.RecipeIngredient;
import com.github.italomded.recipesapi.dto.RecipeIngredientDTO;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientCreateForm;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientEditForm;
import com.github.italomded.recipesapi.service.RecipeIngredientService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Transactional
    @PutMapping("{id}")
    public ResponseEntity<RecipeIngredientDTO> editRecipeIngredient(@PathVariable long id, @RequestBody @Valid RecipeIngredientEditForm form) {
        RecipeIngredient recipeIngredient = recipeIngredientService.editRecipeIngredient(id, form);
        return ResponseEntity.ok(new RecipeIngredientDTO(recipeIngredient));
    }

    @Transactional
    @PostMapping("{recipeId}")
    public ResponseEntity<RecipeIngredientDTO> createRecipeIngredient(@PathVariable long recipeId, @RequestBody @Valid RecipeIngredientCreateForm form,
                                                                      UriComponentsBuilder uriComponentsBuilder) {
        RecipeIngredient recipeIngredient = recipeIngredientService.createRecipeIngredient(recipeId, form);
        URI location = uriComponentsBuilder.path("/api/recipe/{id}").buildAndExpand(recipeIngredient.getRecipe().getID()).toUri();
        return ResponseEntity.created(location).body(new RecipeIngredientDTO(recipeIngredient));
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity deleteRecipeIngredient(@PathVariable long id) {
        recipeIngredientService.deleteRecipeIngredient(id);
        return ResponseEntity.noContent().build();
    }

}
