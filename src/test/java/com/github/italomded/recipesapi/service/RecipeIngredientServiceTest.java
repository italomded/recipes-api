package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.recipe.*;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientCreateForm;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientEditForm;
import com.github.italomded.recipesapi.repository.IngredientRepository;
import com.github.italomded.recipesapi.repository.RecipeIngredientRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.*;

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
        Recipe recipe = RecipeServiceTest.createRecipe();
        Ingredient ingredient = new Ingredient();
        RecipeIngredient recipeIngredientWithId = new RecipeIngredient();

        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(recipe);
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(ingredient);
        Mockito.when(recipeIngredientRepository.save(Mockito.any()))
                .thenReturn(recipeIngredientWithId);

        RecipeIngredientCreateForm form = createRecipeIngredientCreateForm();
        recipeIngredientService.createRecipeIngredient(Mockito.anyLong(),form, recipe.getCreatorUser());

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
    void shouldThrowAExceptionOnCreateRecipeIngredientIfFatherEntitiesNeededDoesntExists() {
        RecipeIngredientCreateForm form = createRecipeIngredientCreateForm();

        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> recipeIngredientService.createRecipeIngredient(1L, form, new ApplicationUser()));
    }

    @Test
    void shouldEditRecipeIngredient() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(
                RecipeServiceTest.createRecipe(), null, new Quantity(), null, null
        );

        Mockito.when(recipeIngredientRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(recipeIngredient);

        RecipeIngredientEditForm form = createRecipeIngredientEditForm();
        recipeIngredientService.editRecipeIngredient(1L, form, recipeIngredient.getRecipe().getCreatorUser());

        Mockito.verify(recipeIngredientRepository).save(Mockito.any());
        Assertions.assertEquals(form.prepareMinutes(), recipeIngredient.getPrepareMinutes());
        Assertions.assertEquals(form.amount(), recipeIngredient.getQuantity().getAmount());
        Assertions.assertEquals(form.measure(), recipeIngredient.getQuantity().getMeasure());
        Assertions.assertEquals(form.instruction(), recipeIngredient.getInstruction());
    }

    @Test
    void shouldThrowAExceptionOnEditRecipeIngredientIfIdDoesntExists() {
        Mockito.when(recipeIngredientRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        RecipeIngredientEditForm form = createRecipeIngredientEditForm();
        Assertions.assertThrows(EntityNotFoundException.class, () -> recipeIngredientService.editRecipeIngredient(1L, form, new ApplicationUser()));
    }

    @Test
    void shouldDeleteARecipeIngredient() {
        Recipe recipe = RecipeServiceTest.createRecipe();
        recipe.addRecipeIngredient(new RecipeIngredient());
        recipe.addRecipeIngredient(new RecipeIngredient());
        recipe.addRecipeIngredient(new RecipeIngredient());

        RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, null, null, null, null);
        recipe.addRecipeIngredient(recipeIngredient);

        Mockito.when(recipeIngredientRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(recipeIngredient);

        recipeIngredientService.deleteRecipeIngredient(1L, recipe.getCreatorUser());

        Mockito.verify(recipeRepository).save(Mockito.any());
        Mockito.verify(recipeIngredientRepository).delete(Mockito.any());
        Assertions.assertEquals(3, recipe.getIngredientsOfRecipe().size());
    }

    @Test
    void shouldThrowAExceptionOnDeleteARecipeIngredientIfIdDoesntExist() {
        Mockito.when(recipeIngredientRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> recipeIngredientService.deleteRecipeIngredient(1L, new ApplicationUser()));
    }

    @Test
    void shouldThrowAExceptionOnDeleteRecipeIngredientIfRecipeHaveLessThanFourIngredients() {
        Recipe recipe = RecipeServiceTest.createRecipe();
        recipe.addRecipeIngredient(new RecipeIngredient());

        RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, null, null, null, null);
        recipe.addRecipeIngredient(recipeIngredient);

        Mockito.when(recipeIngredientRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(recipeIngredient);

        Assertions.assertThrows(BusinessRuleException.class, () -> recipeIngredientService.deleteRecipeIngredient(1L, recipe.getCreatorUser()));
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
