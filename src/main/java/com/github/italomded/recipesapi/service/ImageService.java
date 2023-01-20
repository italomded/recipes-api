package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.Image;
import com.github.italomded.recipesapi.domain.Recipe;
import com.github.italomded.recipesapi.dto.form.ImageForm;
import com.github.italomded.recipesapi.repository.ImageRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import com.github.italomded.recipesapi.service.exception.EntityDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Image editImage(Long imageID, ImageForm form) {
        // TODO: verify if request user is the author of the recipe
        Optional<Image> optionalImage = imageRepository.findById(imageID);
        if (optionalImage.isEmpty()) {
            throw new EntityDoesNotExistException(Image.class, imageID);
        }

        Image image = optionalImage.get();
        image.setImageBytes(form.bytes());
        imageRepository.save(image);
        return image;
    }

    public Image createImage(Long recipeID, ImageForm form) {
        // TODO: verify if request user is the author of the recipe
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeID);
        if (optionalRecipe.isEmpty()) {
            throw new EntityDoesNotExistException(Recipe.class, recipeID);
        }

        Recipe recipe = optionalRecipe.get();
        Image image = new Image(form.bytes(), recipe);
        recipe.addImage(image);

        recipeRepository.save(recipe);
        image = imageRepository.save(image);
        return image;
    }

    public void deleteImage(Long imageID) {
        // TODO: verify if request user is the author of the recipe
        Optional<Image> optionalImage = imageRepository.findById(imageID);
        if (optionalImage.isEmpty()) {
            throw new EntityDoesNotExistException(Image.class, imageID);
        }

        Image image = optionalImage.get();
        image.getRecipe().removeImage(image);
        recipeRepository.save(image.getRecipe());
        imageRepository.delete(image);
    }
}
