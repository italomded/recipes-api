package com.github.italomded.recipesapi.repository;

import com.github.italomded.recipesapi.domain.recipe.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Page<Image> findByRecipe_ID(Long recipeID, Pageable pageable);
}
