package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.recipe.Ingredient;
import com.github.italomded.recipesapi.domain.recipe.Measure;
import com.github.italomded.recipesapi.domain.recipe.Recipe;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.dto.form.*;
import com.github.italomded.recipesapi.repository.*;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;

public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

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
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(new Ingredient());

        Recipe recipe = new Recipe();
        Mockito.when(recipeRepository.save(Mockito.any()))
                .thenReturn(recipe);

        RecipeCreateForm form = createRecipeForm();
        recipeService.createRecipe(form, new ApplicationUser());

        Mockito.verify(recipeRepository, Mockito.atMost(2)).save(captor.capture());
        recipe = captor.getAllValues().get(0);
        Assertions.assertEquals(form.title(), recipe.getTitle());
        Assertions.assertEquals(form.description(), recipe.getDescription());

        recipe = captor.getAllValues().get(1);
        Assertions.assertEquals(1, recipe.getImages().size());
        Assertions.assertEquals(3, recipe.getIngredientsOfRecipe().size());
    }

    @Test
    void shouldThrowExceptionIfIngredientDoesntExistOnCreateRecipe() {
        Mockito.when(recipeRepository.save(Mockito.any()))
                .thenReturn(new Recipe());
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        RecipeCreateForm recipeForm = createRecipeForm();
        Assertions.assertThrows(EntityNotFoundException.class, () -> recipeService.createRecipe(recipeForm, new ApplicationUser()));
    }

    @Test
    void shouldEditARecipe() {
        Recipe recipe = this.createRecipe();
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong())).thenReturn(recipe);

        RecipeEditForm form = new RecipeEditForm("new title", "new description");
        recipeService.editRecipe(Mockito.anyLong(), form, recipe.getCreatorUser());

        Mockito.verify(recipeRepository).save(recipe);
        Assertions.assertEquals(recipe.getTitle(), form.title());
        Assertions.assertEquals(recipe.getDescription(), form.description());
    }

    @Test
    void shouldThrowAExceptionOnEditRecipeIfRecipeIdIsNull() {
        Mockito.when(recipeRepository.getReferenceById(null)).thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> recipeService.editRecipe(null, new RecipeEditForm("A", "B"), new ApplicationUser()));
    }

    @Test
    void shouldThrowExceptionOnDeleteRecipeIfIdDoesntExists() {
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class, () -> recipeService.deleteRecipe(Mockito.anyLong(), new ApplicationUser()));
    }

    @Test
    void shouldDeleteARecipe() {
        Recipe recipe = this.createRecipe();
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(recipe);
        recipeService.deleteRecipe(Mockito.anyLong(), recipe.getCreatorUser());
        Mockito.verify(recipeRepository).delete(recipe);
    }

    @Test
    void shouldCompareIfAPassedUserIsTheAuthorOfAPassedRecipe() throws IllegalAccessException, NoSuchFieldException {
        ApplicationUser userA = new ApplicationUser();
        ApplicationUser userB = new ApplicationUser();
        Field id = ApplicationUser.class.getDeclaredField("ID");
        id.setAccessible(true);
        id.set(userA, 1L);
        id.set(userB, 2L);

        Recipe recipe = new Recipe("title", "description", userA);

        Assertions.assertTrue(recipeService.verifyIfIsTheAuthor(recipe, userA));
        Assertions.assertThrows(BusinessRuleException.class, () -> recipeService.verifyIfIsTheAuthor(recipe, userB));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }

    private RecipeCreateForm createRecipeForm() {
        ImageForm[] images = {
                new ImageForm("www.someimages.com/niceimage"), new ImageForm("www.someimages.com/niceimage"), new ImageForm("www.someimages.com/niceimage")
        };
        RecipeIngredientCreateForm[] recipeIngredients = {
                new RecipeIngredientCreateForm(50.0, Measure.G, "Do this and do that", 5, 1L),
                new RecipeIngredientCreateForm(2.0, Measure.KG, "Do this and do that", 5, 2L),
                new RecipeIngredientCreateForm(20.0, Measure.MG, "Do this and do that", 5, 3L)
        };
        return new RecipeCreateForm(images, "Simple title", "Simple description", recipeIngredients);
    }

    static public Recipe createRecipe() {
        return new Recipe("title", "description", new ApplicationUser());
    }
}
