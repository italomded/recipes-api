package com.github.italomded.recipesapi.domain;

import com.github.italomded.recipesapi.builder.ApplicationUserBuilder;
import com.github.italomded.recipesapi.builder.ImageBuilder;
import com.github.italomded.recipesapi.builder.RecipeBuilder;
import com.github.italomded.recipesapi.builder.RecipeIngredientBuilder;
import com.github.italomded.recipesapi.domain.recipe.Image;
import com.github.italomded.recipesapi.domain.recipe.Recipe;
import com.github.italomded.recipesapi.domain.recipe.RecipeIngredient;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class RecipeTest {
    private Recipe recipe;

    @BeforeEach
    public void createRecipe() {
        recipe = RecipeBuilder.builder()
                .withId(1L)
                .withRecipeIngredient(RecipeIngredientBuilder.builder().withId(1L).withSequence(1).build())
                .withRecipeIngredient(RecipeIngredientBuilder.builder().withId(2L).withSequence(2).build())
                .withRecipeIngredient(RecipeIngredientBuilder.builder().withId(3L).withSequence(3).build())
                .withRecipeIngredient(RecipeIngredientBuilder.builder().withId(4L).withSequence(4).build())
                .build();
    }

    @Test
    @DisplayName("Should reorganize recipe ingredients after a middle remove")
    void scenario1() {
        //given
        RecipeIngredient recipeIngredient = recipe.getIngredientsOfRecipe().get(1);

        //when
        boolean result = recipe.removeRecipeIngredient(recipeIngredient);

        //then
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(3, recipe.getIngredientsOfRecipe().size());
        validateSequence(recipe.getIngredientsOfRecipe());
    }

    @Test
    @DisplayName("Should reorganize recipe ingredients after a first element remove")
    void scenario2() {
        //given
        RecipeIngredient recipeIngredient = recipe.getIngredientsOfRecipe().get(0);

        //when
        recipe.removeRecipeIngredient(recipeIngredient);

        //then
        validateSequence(recipe.getIngredientsOfRecipe());
    }

    @Test
    @DisplayName("Should don't remove a recipe ingredient if the list has only 3 elements")
    void scenario3() {
        //given
        RecipeIngredient recipeIngredientSuccessfully = recipe.getIngredientsOfRecipe().get(0);
        RecipeIngredient recipeIngredientFail = recipe.getIngredientsOfRecipe().get(1);

        //when
        boolean result = recipe.removeRecipeIngredient(recipeIngredientSuccessfully);
        boolean secondResult = recipe.removeRecipeIngredient(recipeIngredientFail);

        //then
        Assertions.assertEquals(3, recipe.getIngredientsOfRecipe().size());
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(false, secondResult);
    }

    @Test
    @DisplayName("Should add new recipe ingredients at any position on the list and reorganize the sequence")
    void scenario4() {
        //given
        RecipeIngredient recipeIngredientAtMiddle = RecipeIngredientBuilder.builder().withId(5L).withSequence(2).build();
        RecipeIngredient recipeIngredientAtStart = RecipeIngredientBuilder.builder().withId(6L).withSequence(1).build();
        RecipeIngredient recipeIngredientAtEnd = RecipeIngredientBuilder.builder().withId(7L).withSequence(5).build();
        RecipeIngredient recipeIngredientAtAny = RecipeIngredientBuilder.builder().withId(8L).withSequence(50).build();

        //when
        recipe.addRecipeIngredient(recipeIngredientAtMiddle, recipeIngredientAtMiddle.getSequence());
        recipe.addRecipeIngredient(recipeIngredientAtStart, recipeIngredientAtStart.getSequence());
        recipe.addRecipeIngredient(recipeIngredientAtEnd, recipeIngredientAtEnd.getSequence());
        recipe.addRecipeIngredient(recipeIngredientAtAny, recipeIngredientAtAny.getSequence());

        //then
        Assertions.assertEquals(8, recipe.getIngredientsOfRecipe().size());
        validateSequence(recipe.getIngredientsOfRecipe());
    }

    @Test
    @DisplayName("Should like and unlike a recipe")
    void scenario5() {
        //given
        Recipe recipe = RecipeBuilder.builder().withId(1L).build();
        ApplicationUser user = ApplicationUserBuilder.builder().withId(1L).build();

        //when
        recipe.likeRecipe(user);
        int likes1moment = recipe.getLikes();
        recipe.likeRecipe(user);
        int likes2moment = recipe.getLikes();

        //then
        Assertions.assertEquals(1, likes1moment);
        Assertions.assertEquals(0, likes2moment);
    }

    @Test
    @DisplayName("Should not remove the image of the recipe if she as the only")
    void scenario6() {
        //given
        Image image = ImageBuilder.builder().withId(1L).build();
        Recipe recipe = RecipeBuilder.builder().withId(1L).withImage(image).build();

        //when
        boolean removed = recipe.removeImage(image);

        //then
        Assertions.assertFalse(removed);
        Assertions.assertEquals(1, recipe.getImages().size());
    }

    private void validateSequence(List<RecipeIngredient> ingredientsOfRecipe) {
        for (int index = 0; index < ingredientsOfRecipe.size() ; index++) {
            Assertions.assertEquals(index + 1, ingredientsOfRecipe.get(index).getSequence());
        }
    }
}
