package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.*;
import com.github.italomded.recipesapi.dto.form.ImageForm;
import com.github.italomded.recipesapi.dto.form.RecipeCreateForm;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientCreateForm;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientCreateWithRecipeForm;
import com.github.italomded.recipesapi.repository.ApplicationUserRepository;
import com.github.italomded.recipesapi.repository.IngredientRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import com.github.italomded.recipesapi.service.exception.EntityDoesNotExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.util.Optional;

public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @InjectMocks
    private RecipeService recipeService;

    AutoCloseable closeable;

    @Captor
    ArgumentCaptor<Recipe> captor;

    @BeforeEach
    public void createMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateARecipe() throws IllegalAccessException, NoSuchFieldException {
        Mockito.when(applicationUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new ApplicationUser()));
        Mockito.when(ingredientRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new Ingredient()));

        Recipe recipe = new Recipe();
        Field id = Recipe.class.getDeclaredField("ID");
        id.setAccessible(true);
        id.set(recipe, 1L);

        Mockito.when(recipeRepository.save(Mockito.any()))
                .thenReturn(recipe);

        RecipeCreateForm form = createRecipeForm();
        recipeService.createRecipe(form);

        Mockito.verify(recipeRepository).save(captor.capture());
        recipe = captor.getValue();

        Assertions.assertEquals(form.title(), recipe.getTitle());
        Assertions.assertEquals(form.description(), recipe.getDescription());
        Assertions.assertEquals(1, recipe.getImages().size());
        Assertions.assertEquals(3, recipe.getIngredientsOfRecipe().size());
    }

    @Test
    void shouldThrowExceptionIfIngredientDoesntExist() {
        Mockito.when(applicationUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new ApplicationUser()));
        Mockito.when(ingredientRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        RecipeCreateForm recipeForm = createRecipeForm();
        Assertions.assertThrows(EntityDoesNotExistException.class, () -> recipeService.createRecipe(recipeForm));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }

    private RecipeCreateForm createRecipeForm() {
        ImageForm[] images = {
                new ImageForm("0x12345".getBytes()), new ImageForm("0x12345".getBytes()), new ImageForm("0x12345".getBytes())
        };
        RecipeIngredientCreateWithRecipeForm[] recipeIngredients = {
                new RecipeIngredientCreateWithRecipeForm(1L, 50.0, Measure.G, "Do this and do that", 5),
                new RecipeIngredientCreateWithRecipeForm(2L, 2.0, Measure.KG, "Do this and do that", 5),
                new RecipeIngredientCreateWithRecipeForm(3L, 20.0, Measure.MG, "Do this and do that", 5)
        };
        return new RecipeCreateForm(images, "Simple title", "Simple description", recipeIngredients);
    }
}
