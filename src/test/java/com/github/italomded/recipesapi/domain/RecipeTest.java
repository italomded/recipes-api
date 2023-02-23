package com.github.italomded.recipesapi.domain;

import com.github.italomded.recipesapi.domain.recipe.Image;
import com.github.italomded.recipesapi.domain.recipe.Recipe;
import com.github.italomded.recipesapi.domain.recipe.RecipeIngredient;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecipeTest {
    private Recipe recipe;
    private Field recipeIngredientID;

    @BeforeAll
    public void getIdField() throws NoSuchFieldException {
        recipeIngredientID = RecipeIngredient.class.getDeclaredField("ID");
        recipeIngredientID.setAccessible(true);
    }

    @BeforeEach
    public void createRecipe() throws IllegalAccessException {
        recipe = new Recipe();
        List<RecipeIngredient> recipeIngredients = Arrays.asList(new RecipeIngredient(), new RecipeIngredient(), new RecipeIngredient());

        int counter = 0;
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            int x = ++counter;
            recipeIngredient.setSequence(x);
            recipeIngredientID.set(recipeIngredient, (long) x);
            recipe.addRecipeIngredient(recipeIngredient);
        }
    }

    @Test
    void shouldRemoveAndReorganizeRecipeIngredients() throws IllegalAccessException {
        RecipeIngredient recipeIngredient = recipe.getIngredientsOfRecipe().get(1);
        RecipeIngredient recipeIngredientToAdd = new RecipeIngredient();
        recipeIngredientID.set(recipeIngredientToAdd, 10L);
        recipe.addRecipeIngredient(recipeIngredientToAdd);
        boolean result = recipe.removeRecipeIngredient(recipeIngredient);

        Assertions.assertEquals(true, result);
        Assertions.assertEquals(3, recipe.getIngredientsOfRecipe().size());
        Assertions.assertEquals(1, recipe.getIngredientsOfRecipe().get(0).getSequence());
        Assertions.assertEquals(2, recipe.getIngredientsOfRecipe().get(1).getSequence());
        Assertions.assertEquals(3, recipe.getIngredientsOfRecipe().get(2).getSequence());

        recipeIngredient = recipe.getIngredientsOfRecipe().get(0);
        recipe.removeRecipeIngredient(recipeIngredient);

        Assertions.assertEquals(1, recipe.getIngredientsOfRecipe().get(0).getSequence());
    }

    @Test
    void shouldNotRemoveImagesIfHaveOnlyThree() {
        RecipeIngredient recipeIngredient = recipe.getIngredientsOfRecipe().get(1);
        boolean result = recipe.removeRecipeIngredient(recipeIngredient);
        Assertions.assertEquals(3, recipe.getIngredientsOfRecipe().size());
        Assertions.assertEquals(false, result);
    }

    @Test
    void shouldAddNewIngredientAndReorganizeRecipeIngredients() throws IllegalAccessException {
        RecipeIngredient recipeIngredient1 = new RecipeIngredient();
        recipeIngredientID.set(recipeIngredient1, 4L);
        recipeIngredient1.setSequence(4);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient();
        recipeIngredientID.set(recipeIngredient2, 5L);
        recipeIngredient2.setSequence(37);

        RecipeIngredient recipeIngredient3 = new RecipeIngredient();
        recipeIngredientID.set(recipeIngredient3, 6L);

        recipe.addRecipeIngredient(recipeIngredient1);
        recipe.addRecipeIngredient(recipeIngredient2);
        recipe.addRecipeIngredient(recipeIngredient3);

        Assertions.assertEquals(6, recipe.getIngredientsOfRecipe().size());
        Assertions.assertEquals(recipeIngredient1, recipe.getIngredientsOfRecipe().get(3));
        Assertions.assertEquals(recipeIngredient2, recipe.getIngredientsOfRecipe().get(4));
        Assertions.assertEquals(5, recipe.getIngredientsOfRecipe().get(4).getSequence());
        Assertions.assertEquals(6, recipe.getIngredientsOfRecipe().get(5).getSequence());
    }

    @Test
    void shouldAddNewIngredientAtAnyPositionOfRecipeIngredientsList() throws IllegalAccessException {
        RecipeIngredient recipeIngredientAtMiddle = new RecipeIngredient();
        recipeIngredientID.set(recipeIngredientAtMiddle, 4L);
        recipeIngredientAtMiddle.setSequence(2);

        RecipeIngredient recipeIngredientAtStart = new RecipeIngredient();
        recipeIngredientID.set(recipeIngredientAtStart, 5L);
        recipeIngredientAtStart.setSequence(1);

        RecipeIngredient recipeIngredientAtEnd = new RecipeIngredient();
        recipeIngredientID.set(recipeIngredientAtEnd, 6L);
        recipeIngredientAtEnd.setSequence(5);

        recipe.addRecipeIngredient(recipeIngredientAtMiddle);
        recipe.addRecipeIngredient(recipeIngredientAtStart);
        recipe.addRecipeIngredient(recipeIngredientAtEnd);

        Assertions.assertEquals(6, recipe.getIngredientsOfRecipe().size());
        int counter = 0;
        for (RecipeIngredient recipeIngredient : recipe.getIngredientsOfRecipe()) {
            counter += recipeIngredient.getSequence();
            System.out.println(recipeIngredient.getSequence());
        }
        Assertions.assertEquals(21, counter);
    }

    @Test
    void shouldAddWithSequenceOneIfListOfRecipeIngredientsIsNull() {
        Recipe recipeEmpty = new Recipe();
        recipeEmpty.addRecipeIngredient(new RecipeIngredient());
        Assertions.assertEquals(1, recipeEmpty.getIngredientsOfRecipe().size());
        Assertions.assertEquals(1, recipeEmpty.getIngredientsOfRecipe().get(0).getSequence());
    }

    @Test
    void shouldLikeAndUnlikeARecipe() {
        Recipe recipe = new Recipe();
        ApplicationUser user = new ApplicationUser();
        recipe.likeRecipe(user);

        Assertions.assertEquals(1, recipe.getLikes());
        recipe.likeRecipe(user);

        Assertions.assertEquals(0, recipe.getLikes());
    }

    @Test
    void shouldNotRemoveTheImageIfItIsTheOnlyFormOfTheRecipe() {
        Recipe recipe = new Recipe();
        Image image = new Image();
        recipe.addImage(image);

        Assertions.assertEquals(1, recipe.getImages().size());
        Assertions.assertFalse(recipe.removeImage(image));
    }
}
