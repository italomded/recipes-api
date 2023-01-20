package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.*;
import com.github.italomded.recipesapi.dto.form.*;
import com.github.italomded.recipesapi.repository.ApplicationUserRepository;
import com.github.italomded.recipesapi.repository.IngredientRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

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
    void shouldCreateARecipe() {
        Mockito.when(applicationUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new ApplicationUser()));
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(new Ingredient());

        Recipe recipe = new Recipe();
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
    void shouldThrowExceptionIfIngredientDoesntExistOnCreateRecipe() {
        Mockito.when(applicationUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new ApplicationUser()));
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        RecipeCreateForm recipeForm = createRecipeForm();
        Assertions.assertThrows(EntityNotFoundException.class, () -> recipeService.createRecipe(recipeForm));
    }

    @Test
    void shouldEditARecipe() {
        Recipe recipe = new Recipe();
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong())).thenReturn(recipe);

        RecipeEditForm form = new RecipeEditForm("new title", "new description");
        recipeService.editRecipe(Mockito.anyLong(), form);

        Mockito.verify(recipeRepository).save(recipe);
        Assertions.assertEquals(recipe.getTitle(), form.title());
        Assertions.assertEquals(recipe.getDescription(), form.description());
    }

    @Test
    void shouldThrowAExceptionOnEditRecipeIfRecipeIdIsNull() {
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong())).thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> recipeService.editRecipe(Mockito.anyLong(), new RecipeEditForm("A", "B")));
    }

    @Test
    void shouldThrowExceptionOnDeleteRecipeIfIdDoesntExists() {
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class, () -> recipeService.deleteRecipe(Mockito.anyLong()));
    }

    @Test
    void shouldDeleteARecipe() {
        Recipe recipe = new Recipe();
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(recipe);
        recipeService.deleteRecipe(Mockito.anyLong());
        Mockito.verify(recipeRepository).delete(recipe);
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
