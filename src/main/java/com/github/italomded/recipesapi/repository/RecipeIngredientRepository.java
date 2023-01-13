package com.github.italomded.recipesapi.repository;

import com.github.italomded.recipesapi.domain.RecipeIngredient;
import com.github.italomded.recipesapi.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    Page<RecipeService> findByRecipe_ID(Long recipeID, Pageable pageable);
}
