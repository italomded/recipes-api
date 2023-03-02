package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.builder.ApplicationUserBuilder;
import com.github.italomded.recipesapi.builder.IngredientBuilder;
import com.github.italomded.recipesapi.builder.RecipeBuilder;
import com.github.italomded.recipesapi.builder.RecipeIngredientBuilder;
import com.github.italomded.recipesapi.domain.recipe.*;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.dto.form.RecipeIngredientCreateWithSequenceForm;
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
    @DisplayName("Should successfully create a recipe ingredient")
    void scenario1() {
        //given
        ApplicationUser applicationUser = ApplicationUserBuilder.builder().withId(1L).build();
        Recipe recipe = RecipeBuilder.builder().withId(1L).withCreatorUser(applicationUser).build();
        Ingredient ingredient = IngredientBuilder.builder().withId(1L).build();
        var form = createRecipeIngredientCreateForm();

        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(recipe);
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(ingredient);
        Mockito.when(recipeIngredientRepository.save(Mockito.any()))
                .thenReturn(new RecipeIngredient());

        //when
        recipeIngredientService.createRecipeIngredient(recipe.getID(),form, applicationUser);

        //then
        Mockito.verify(recipeIngredientRepository).save(captor.capture());
        Mockito.verify(recipeRepository).save(recipe);
        RecipeIngredient recipeIngredient = captor.getValue();

        Assertions.assertEquals(recipe, recipeIngredient.getRecipe());
        Assertions.assertEquals(ingredient, recipeIngredient.getIngredient());
        Assertions.assertTrue(recipe.getIngredientsOfRecipe().contains(recipeIngredient));
        Assertions.assertEquals(form.getAmount(), recipeIngredient.getQuantity().getAmount());
        Assertions.assertEquals(form.getMeasure(), recipeIngredient.getQuantity().getMeasure());
        Assertions.assertEquals(form.getInstruction(), recipeIngredient.getInstruction());
        Assertions.assertEquals(form.getPrepareMinutes(), recipeIngredient.getPrepareMinutes());
    }

    @Test
    @DisplayName("Should throw an exception when creating a recipe ingredient without an existing recipe")
    void scenario2() {
        //given
        var form = createRecipeIngredientCreateForm();
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        //when,then
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> recipeIngredientService.createRecipeIngredient(1L, form, new ApplicationUser()));
    }

    @Test
    @DisplayName("Should throw an exception when creating a recipe ingredient without an existing ingredient")
    void scenario3() {
        //given
        ApplicationUser applicationUser = ApplicationUserBuilder.builder().withId(1L).build();
        Recipe recipe = RecipeBuilder.builder().withId(1L).withCreatorUser(applicationUser).build();
        var form = createRecipeIngredientCreateForm();

        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(recipe);
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        //when,then
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> recipeIngredientService.createRecipeIngredient(recipe.getID(), form, applicationUser));
    }

    @Test
    @DisplayName("Should successfully edit a recipe ingredient")
    void scenario4() {
        //given
        ApplicationUser applicationUser = ApplicationUserBuilder.builder().withId(1L).build();
        Recipe recipe = RecipeBuilder.builder().withId(1L).withCreatorUser(applicationUser).build();
        RecipeIngredient recipeIngredient = RecipeIngredientBuilder.builder().withId(1L).withRecipe(recipe).build();
        var form = createRecipeIngredientEditForm();

        Mockito.when(recipeIngredientRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(recipeIngredient);

        //when
        recipeIngredientService.editRecipeIngredient(recipeIngredient.getID(), form, applicationUser);

        //then
        Mockito.verify(recipeIngredientRepository).save(Mockito.any());
        Assertions.assertEquals(form.getPrepareMinutes(), recipeIngredient.getPrepareMinutes());
        Assertions.assertEquals(form.getAmount(), recipeIngredient.getQuantity().getAmount());
        Assertions.assertEquals(form.getMeasure(), recipeIngredient.getQuantity().getMeasure());
        Assertions.assertEquals(form.getInstruction(), recipeIngredient.getInstruction());
    }

    @Test
    @DisplayName("Should throw an exception when editing a recipe ingredient whose ID does not exist")
    void scenario5() {
        //given
        Mockito.when(recipeIngredientRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        var form = createRecipeIngredientEditForm();

        //when,then
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> recipeIngredientService.editRecipeIngredient(1L, form, new ApplicationUser()));
    }

    @Test
    @DisplayName("You must successfully delete a recipe ingredient if the recipe has more than 3 ingredients")
    void scenario6() {
        //given
        ApplicationUser applicationUser = ApplicationUserBuilder.builder().withId(1L).build();
        Recipe recipe = RecipeBuilder.builder().withId(1L).withCreatorUser(applicationUser)
                .withRecipeIngredient(RecipeIngredientBuilder.builder().withId(1L).build())
                .withRecipeIngredient(RecipeIngredientBuilder.builder().withId(2L).build())
                .withRecipeIngredient(RecipeIngredientBuilder.builder().withId(3L).build())
                .build();
        var recipeListSize = recipe.getIngredientsOfRecipe().size();

        RecipeIngredient recipeIngredient = RecipeIngredientBuilder.builder().withId(4L).withRecipe(recipe).build();
        recipe.addRecipeIngredient(recipeIngredient);

        Mockito.when(recipeIngredientRepository.getReferenceById(recipeIngredient.getID()))
                .thenReturn(recipeIngredient);

        //when
        recipeIngredientService.deleteRecipeIngredient(recipeIngredient.getID(), applicationUser);

        //then
        Mockito.verify(recipeRepository).save(recipe);
        Mockito.verify(recipeIngredientRepository).delete(recipeIngredient);
        Assertions.assertEquals(recipeListSize, recipe.getIngredientsOfRecipe().size());
    }

    @Test
    @DisplayName("Should throw an exception when trying to delete a recipe ingredient if the recipe has at least 3 ingredients")
    void shouldThrowAExceptionOnDeleteRecipeIngredientIfRecipeHaveLessThanFourIngredients() {
        //given
        ApplicationUser applicationUser = ApplicationUserBuilder.builder().withId(1L).build();
        Recipe recipe = RecipeBuilder.builder().withId(1L).withCreatorUser(applicationUser)
                .withRecipeIngredient(RecipeIngredientBuilder.builder().withId(1L).build())
                .withRecipeIngredient(RecipeIngredientBuilder.builder().withId(2L).build())
                .build();
        RecipeIngredient recipeIngredient = RecipeIngredientBuilder.builder().withId(1L).withRecipe(recipe).build();
        recipe.addRecipeIngredient(recipeIngredient);

        Mockito.when(recipeIngredientRepository.getReferenceById(recipeIngredient.getID()))
                .thenReturn(recipeIngredient);

        //when,then
        Assertions.assertThrows(BusinessRuleException.class,
                () -> recipeIngredientService.deleteRecipeIngredient(recipeIngredient.getID(), applicationUser));
    }

    @Test
    @DisplayName("Should throw an exception when trying to delete a recipe ingredient with an ID that doesn't exist")
    void shouldThrowAExceptionOnDeleteARecipeIngredientIfIdDoesntExist() {
        //given
        Mockito.when(recipeIngredientRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        //when,then
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> recipeIngredientService.deleteRecipeIngredient(1L, new ApplicationUser()));
    }



    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }

    private RecipeIngredientCreateWithSequenceForm createRecipeIngredientCreateForm() {
        var form = new RecipeIngredientCreateWithSequenceForm(
                10.0,
                Measure.TEASPON, "Do this and do that",
                5,
                1L, 4);
        return form;
    }

    private RecipeIngredientEditForm createRecipeIngredientEditForm() {
        var form = new RecipeIngredientEditForm(
                10.0,
                Measure.TEASPON,
                "Do this and do that!",
                10);
        return form;
    }
}
