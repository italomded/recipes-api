package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.Image;
import com.github.italomded.recipesapi.domain.Recipe;
import com.github.italomded.recipesapi.dto.form.ImageForm;
import com.github.italomded.recipesapi.repository.ImageRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    ImageRepository imageRepository;
    RecipeRepository recipeRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, RecipeRepository recipeRepository) {
        this.imageRepository = imageRepository;
        this.recipeRepository = recipeRepository;
    }

    public Page<Image> getImagesByRecipeId(Pageable pageable, Long recipeID) {
        return imageRepository.findByRecipe_ID(recipeID, pageable);
    }

    @Transactional
    public Image editImage(Long imageID, ImageForm form) {
        // TODO: verify if request user is the author of the recipe
        Image image = imageRepository.getReferenceById(imageID);
        image.setImageBytes(form.bytes());
        imageRepository.save(image);
        return image;
    }

    @Transactional
    public Image createImage(Long recipeID, ImageForm form) {
        // TODO: verify if request user is the author of the recipe
        Recipe recipe = recipeRepository.getReferenceById(recipeID);
        Image image = new Image(form.bytes(), recipe);
        recipe.addImage(image);

        recipeRepository.save(recipe);
        image = imageRepository.save(image);
        return image;
    }

    @Transactional
    public void deleteImage(Long imageID) {
        // TODO: verify if request user is the author of the recipe
        Image image = imageRepository.getReferenceById(imageID);
        image.getRecipe().removeImage(image);
        recipeRepository.save(image.getRecipe());
        imageRepository.delete(image);
    }
}
