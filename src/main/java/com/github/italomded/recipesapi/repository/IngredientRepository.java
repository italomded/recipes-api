package com.github.italomded.recipesapi.repository;

import com.github.italomded.recipesapi.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
