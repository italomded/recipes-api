package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.Ingredient;
import com.github.italomded.recipesapi.domain.Measure;
import com.github.italomded.recipesapi.domain.Recipe;
import com.github.italomded.recipesapi.domain.RecipeIngredient;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientCreateForm;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientEditForm;
import com.github.italomded.recipesapi.repository.IngredientRepository;
import com.github.italomded.recipesapi.repository.RecipeIngredientRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import com.github.italomded.recipesapi.service.exception.EntityDoesNotExistException;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Optional;

public class RecipeIngredientServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;
    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeIngredientService recipeIngredientService;
    @Captor
    private ArgumentCaptor<RecipeIngredient> captor;

    private AutoCloseable closeable;

    @BeforeEach
    public void createMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateARecipeIngredient() {
        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        RecipeIngredient recipeIngredientWithId = new RecipeIngredient();

        Mockito.when(recipeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(recipe));
        Mockito.when(ingredientRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(ingredient));
        Mockito.when(recipeIngredientRepository.save(Mockito.any()))
                .thenReturn(recipeIngredientWithId);

        RecipeIngredientCreateForm form = createRecipeIngredientCreateForm();
        recipeIngredientService.createRecipeIngredient(Mockito.anyLong(),form);

        Mockito.verify(recipeIngredientRepository).save(captor.capture());
        Mockito.verify(recipeRepository).save(recipe);

        RecipeIngredient recipeIngredient = captor.getValue();
        Assertions.assertEquals(recipe, recipeIngredient.getRecipe());
        Assertions.assertEquals(ingredient, recipeIngredient.getIngredient());
        Assertions.assertTrue(recipe.getIngredientsOfRecipe().contains(recipeIngredient));
        Assertions.assertEquals(form.amount(), recipeIngredient.getQuantity().getAmount());
        Assertions.assertEquals(form.measure(), recipeIngredient.getQuantity().getMeasure());
        Assertions.assertEquals(form.instruction(), recipeIngredient.getInstruction());
        Assertions.assertEquals(form.prepareMinutes(), recipeIngredient.getPrepareMinutes());
    }

    @Test
    void shouldThrowAExceptionOnCreateRecipeIngredientIfFatherEntitiesNeededExists() {
        RecipeIngredientCreateForm form = createRecipeIngredientCreateForm();

        Mockito.when(recipeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityDoesNotExistException.class, () -> recipeIngredientService.createRecipeIngredient(1L, form));

        Mockito.when(recipeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new Recipe()));
        Mockito.when(ingredientRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityDoesNotExistException.class, () -> recipeIngredientService.createRecipeIngredient(1L, form));
    }

    @Test
    void shouldEditRecipeIngredient() {
        RecipeIngredient recipeIngredient = new RecipeIngredient();

        Mockito.when(recipeIngredientRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(recipeIngredient));

        RecipeIngredientEditForm form = createRecipeIngredientEditForm();
        recipeIngredientService.editRecipeIngredient(1L, form);

        Mockito.verify(recipeIngredientRepository).save(Mockito.any());
        Assertions.assertEquals(form.prepareMinutes(), recipeIngredient.getPrepareMinutes());
        Assertions.assertEquals(form.amount(), recipeIngredient.getQuantity().getAmount());
        Assertions.assertEquals(form.measure(), recipeIngredient.getQuantity().getMeasure());
        Assertions.assertEquals(form.instruction(), recipeIngredient.getInstruction());
    }

    @Test
    void shouldThrowAExceptionOnEditRecipeIngredientIfIdDoesntExists() {
        Mockito.when(recipeIngredientRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        RecipeIngredientEditForm form = createRecipeIngredientEditForm();
        Assertions.assertThrows(EntityDoesNotExistException.class, () -> recipeIngredientService.editRecipeIngredient(1L, form));
    }

    @Test
    void shouldDeleteARecipeIngredient() {
        Recipe recipe = new Recipe();
        recipe.addRecipeIngredient(new RecipeIngredient());
        recipe.addRecipeIngredient(new RecipeIngredient());
        recipe.addRecipeIngredient(new RecipeIngredient());

        RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, null, null, null, null);
        recipe.addRecipeIngredient(recipeIngredient);

        Mockito.when(recipeIngredientRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(recipeIngredient));

        recipeIngredientService.deleteRecipeIngredient(1L);

        Mockito.verify(recipeRepository).save(Mockito.any());
        Mockito.verify(recipeIngredientRepository).delete(Mockito.any());
        Assertions.assertEquals(3, recipe.getIngredientsOfRecipe().size());
    }

    @Test
    void shouldThrowAExceptionOnDeleteARecipeIngredientIfIdDoesntExist() {
        Mockito.when(recipeIngredientRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityDoesNotExistException.class, () -> recipeIngredientService.deleteRecipeIngredient(1L));
    }

    @Test
    void shouldThrowAExceptionOnDeleteRecipeIngredientIfRecipeHaveLessThanFourIngredients() {
        Recipe recipe = new Recipe();
        recipe.addRecipeIngredient(new RecipeIngredient());

        RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, null, null, null, null);
        recipe.addRecipeIngredient(recipeIngredient);

        Mockito.when(recipeIngredientRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(recipeIngredient));

        Assertions.assertThrows(BusinessRuleException.class, () -> recipeIngredientService.deleteRecipeIngredient(1L));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }

    private RecipeIngredientCreateForm createRecipeIngredientCreateForm() {
        RecipeIngredientCreateForm form = new RecipeIngredientCreateForm(
                1L,
                10.0, Measure.TEASPON,
                "Do this and do that",
                5, 4);
        return form;
    }

    private RecipeIngredientEditForm createRecipeIngredientEditForm() {
        RecipeIngredientEditForm form = new RecipeIngredientEditForm(10.0, Measure.TEASPON, "Do this and do that!", 10);
        return form;
    }
}
