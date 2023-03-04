package com.github.italomded.recipesapi.repository;

import com.github.italomded.recipesapi.domain.recipe.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query(value = """
            select r from recipe r
            join r.ingredientsOfRecipe ri
            join ri.ingredient i
            where
            i.ID in :ingredients
            and 0 = (
                select count(i2.ID) from ingredient i2
                join i2.recipesThatUse ri2
                join ri2.recipe r2
                where r = r2
                and i2.ID not in :ingredients
            )
            group by r
            """)
    Page<Recipe> findByIngredients(Long[] ingredients, Pageable pageable);

    Page<Recipe> findByCreatorUser_ID(Long userID, Pageable pageable);
}
