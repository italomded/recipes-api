package com.github.italomded.recipesapi.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class RecipeTest {
    private Recipe recipe;
    private static Field recipeIngredientID;

    @BeforeAll
    public static void getIdField() throws NoSuchFieldException {
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
        }

        recipe.getIngredientsOfRecipe().addAll(recipeIngredients);
    }

    @Test
    void shouldRemoveAndReorganizeRecipeIngredients() {
        RecipeIngredient recipeIngredient = recipe.getIngredientsOfRecipe().get(1);
        recipe.removeRecipeIngredient(recipeIngredient);

        Assertions.assertEquals(2, recipe.getIngredientsOfRecipe().size());
        Assertions.assertEquals(1, recipe.getIngredientsOfRecipe().get(0).getSequence());
        Assertions.assertEquals(2, recipe.getIngredientsOfRecipe().get(1).getSequence());

        recipeIngredient = recipe.getIngredientsOfRecipe().get(0);
        recipe.removeRecipeIngredient(recipeIngredient);

        Assertions.assertEquals(1, recipe.getIngredientsOfRecipe().get(0).getSequence());
    }

    @Test
    void shouldAddNewIngredientAndReorganizeRecipeIngredients() throws IllegalAccessException {
        RecipeIngredient recipeIngredient1 = new RecipeIngredient();
        recipeIngredientID.set(recipeIngredient1, 4L);
        recipeIngredient1.setSequence(4);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient();
        recipeIngredientID.set(recipeIngredient2, 5L);
        recipeIngredient2.setSequence(37);

        recipe.addRecipeIngredient(recipeIngredient1);
        recipe.addRecipeIngredient(recipeIngredient2);

        Assertions.assertEquals(5, recipe.getIngredientsOfRecipe().size());
        Assertions.assertEquals(recipeIngredient1, recipe.getIngredientsOfRecipe().get(3));
        Assertions.assertEquals(recipeIngredient2, recipe.getIngredientsOfRecipe().get(4));
        Assertions.assertEquals(5, recipe.getIngredientsOfRecipe().get(4).getSequence());
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
}
