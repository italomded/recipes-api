package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.ApplicationUser;
import com.github.italomded.recipesapi.domain.Image;
import com.github.italomded.recipesapi.domain.Recipe;
import com.github.italomded.recipesapi.dto.form.ImageForm;
import com.github.italomded.recipesapi.repository.ImageRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
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
    public Image editImage(Long imageID, ImageForm form, ApplicationUser userAuthor) {
        Image image = imageRepository.getReferenceById(imageID);
        RecipeService.verifyIfIsTheAuthor(image.getRecipe(), userAuthor);

        image.setImageBytes(form.bytes());
        imageRepository.save(image);
        return image;
    }

    @Transactional
    public Image createImage(Long recipeID, ImageForm form, ApplicationUser userAuthor) {
        Recipe recipe = recipeRepository.getReferenceById(recipeID);
        RecipeService.verifyIfIsTheAuthor(recipe, userAuthor);

        Image image = new Image(form.bytes(), recipe);
        recipe.addImage(image);

        recipeRepository.save(recipe);
        image = imageRepository.save(image);
        return image;
    }

    @Transactional
    public void deleteImage(Long imageID, ApplicationUser userAuthor) {
        Image image = imageRepository.getReferenceById(imageID);
        RecipeService.verifyIfIsTheAuthor(image.getRecipe(), userAuthor);

        boolean removed = image.getRecipe().removeImage(image);
        if (removed) {
            recipeRepository.save(image.getRecipe());
            imageRepository.delete(image);
        } else {
            throw new BusinessRuleException(
                    Recipe.class,
                    "can't delete recipe image because the image list should have at least 1 image or the image does not belong in the recipe");
        }
    }
}
