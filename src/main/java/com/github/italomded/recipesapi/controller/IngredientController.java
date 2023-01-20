package com.github.italomded.recipesapi.controller;

import com.github.italomded.recipesapi.domain.Ingredient;
import com.github.italomded.recipesapi.dto.IngredientDTO;
import com.github.italomded.recipesapi.dto.form.IngredientForm;
import com.github.italomded.recipesapi.service.IngredientService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

    @Transactional
    @PutMapping("{id}")
    public ResponseEntity<IngredientDTO> editRecipe(@PathVariable long id, @RequestBody @Valid IngredientForm form) {
        Ingredient ingredient = ingredientService.editIngredient(id, form);
        return ResponseEntity.ok(new IngredientDTO(ingredient));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<IngredientDTO> createIngredient(@RequestBody @Valid IngredientForm form, UriComponentsBuilder uriComponentsBuilder) {
        Ingredient ingredient = ingredientService.createIngredient(form);
        URI location = uriComponentsBuilder.path("/api/ingredient").build().toUri();
        return ResponseEntity.created(location).body(new IngredientDTO(ingredient));
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity deleteIngredient(@PathVariable long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
