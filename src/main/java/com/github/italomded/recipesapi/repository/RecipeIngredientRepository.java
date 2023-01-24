package com.github.italomded.recipesapi.repository;

import com.github.italomded.recipesapi.domain.recipe.RecipeIngredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    Page<RecipeIngredient> findByRecipe_ID(Long recipeID, Pageable pageable);
}
