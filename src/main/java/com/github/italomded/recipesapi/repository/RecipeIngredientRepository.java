package com.github.italomded.recipesapi.repository;

import com.github.italomded.recipesapi.domain.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}
