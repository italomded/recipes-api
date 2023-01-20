package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.Ingredient;
import com.github.italomded.recipesapi.dto.form.IngredientForm;
import com.github.italomded.recipesapi.repository.IngredientRepository;
import com.github.italomded.recipesapi.service.exception.DataValidationException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public Ingredient createIngredient(IngredientForm form) {
        // TODO: verify if user author of request are adm
        verifyIfIngredientNameAlreadyExists(form);

        Ingredient ingredient = new Ingredient(form.name(), form.type());
        ingredient = ingredientRepository.save(ingredient);
        return ingredient;
    }

    @Transactional
    public Ingredient editIngredient(Long ingredientID, IngredientForm form) {
        // TODO: verify if user author of request are adm
        Ingredient ingredient = ingredientRepository.getReferenceById(ingredientID);
        verifyIfIngredientNameAlreadyExists(form);

        ingredient.setName(form.name());
        ingredient.setCategory(form.type());
        ingredient = ingredientRepository.save(ingredient);
        return ingredient;
    }

    @Transactional
    public boolean deleteIngredient(Long ingredientID) {
        // TODO: verify if user author of request are adm
        Ingredient ingredient = ingredientRepository.getReferenceById(ingredientID);
        ingredientRepository.delete(ingredient);
        return true;
    }

    private void verifyIfIngredientNameAlreadyExists(IngredientForm form) {
        boolean exists = ingredientRepository.existsByName(form.name());
        if (exists) {
            try {
                Field name = form.getClass().getDeclaredField("name");
                throw new DataValidationException("ingredient name already exists", name);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
