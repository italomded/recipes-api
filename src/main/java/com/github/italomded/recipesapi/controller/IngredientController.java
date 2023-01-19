package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.domain.Ingredient;
import com.github.italomded.recipesapi.dto.IngredientDTO;
import com.github.italomded.recipesapi.dto.form.IngredientForm;
import com.github.italomded.recipesapi.service.IngredientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/ingredient")
public class IngredientController {
    private IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<Page<IngredientDTO>> getAllIngredients(Pageable pageable) {
        Page<Ingredient> allIngredients = ingredientService.getAllIngredients(pageable);
        Page<IngredientDTO> allIngredientsDTO = allIngredients.map(IngredientDTO::new);
        return ResponseEntity.ok(allIngredientsDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<Long> editRecipe(@PathVariable long id, @RequestBody @Valid IngredientForm form) {
        Long idEdited = ingredientService.editIngredient(id, form);
        return ResponseEntity.ok(idEdited);
    }

    @PostMapping
    public ResponseEntity<Long> createIngredient(@RequestBody @Valid IngredientForm form) {
        Long idCreated = ingredientService.createIngredient(form);
        URI location = URI.create(String.format("/api/ingredient/%d", idCreated));
        return ResponseEntity.created(location).body(idCreated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteIngredient(@PathVariable long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.ok().build();
    }
}
