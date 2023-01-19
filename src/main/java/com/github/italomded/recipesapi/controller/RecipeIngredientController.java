package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.dto.form.RecipeIngredientCreateForm;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientEditForm;
import com.github.italomded.recipesapi.service.RecipeIngredientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Long> editRecipeIngredient(@PathVariable long id, @RequestBody @Valid RecipeIngredientEditForm form) {
        Long idEdited = recipeIngredientService.editRecipeIngredient(id, form);
        return ResponseEntity.ok(idEdited);
    }

    @PostMapping("{recipeId}")
    public ResponseEntity<Long> createRecipeIngredient(@PathVariable long recipeId, @RequestBody @Valid RecipeIngredientCreateForm form) {
        Long idCreated = recipeIngredientService.createRecipeIngredient(recipeId, form);
        URI location = URI.create(String.format("/api/recipe/%d", recipeId));
        return ResponseEntity.created(location).body(idCreated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteRecipeIngredient(@PathVariable long id) {
        recipeIngredientService.deleteRecipeIngredient(id);
        return ResponseEntity.ok().build();
    }

}
