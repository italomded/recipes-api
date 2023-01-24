package com.github.italomded.recipesapi.repository;

import com.github.italomded.recipesapi.domain.recipe.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findByCreatorUser_ID(Long userID, Pageable pageable);
}
