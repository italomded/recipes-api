package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.Ingredient;
import com.github.italomded.recipesapi.dto.form.IngredientForm;
import com.github.italomded.recipesapi.repository.IngredientRepository;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import com.github.italomded.recipesapi.service.exception.DataValidationException;
import com.github.italomded.recipesapi.service.exception.EntityDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;

@Service
public class IngredientService {
    private IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Page<Ingredient> getAllIngredients(Pageable pageable) {
        return ingredientRepository.findAll(pageable);
    }

    public Long createIngredient(IngredientForm form) {
        // TODO: verify if user author of request are adm
        verifyIfIngredientNameAlreadyExists(form);

        Ingredient ingredient = new Ingredient(form.name(), form.type());
        ingredient = ingredientRepository.save(ingredient);
        return ingredient.getID();
    }

    public Long editIngredient(Long ingredientID, IngredientForm form) {
        // TODO: verify if user author of request are adm
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientID);
        if (optionalIngredient.isEmpty()) {
            throw new EntityDoesNotExistException(Ingredient.class, ingredientID);
        }

        verifyIfIngredientNameAlreadyExists(form);

        Ingredient ingredient = new Ingredient(form.name(), form.type());
        ingredient = ingredientRepository.save(ingredient);
        return ingredient.getID();
    }

    public void deleteIngredient(Long ingredientID) {
        // TODO: verify if user author of request are adm
        ingredientRepository.deleteById(ingredientID);
    }

    private void verifyIfIngredientNameAlreadyExists(IngredientForm form) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByName(form.name());
        if (optionalIngredient.isPresent()) {
            try {
                Field name = form.getClass().getDeclaredField("name");
                throw new DataValidationException("Ingredient name already exists", name);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
