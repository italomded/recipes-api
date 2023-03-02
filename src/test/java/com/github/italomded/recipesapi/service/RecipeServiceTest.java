package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.builder.ApplicationUserBuilder;
import com.github.italomded.recipesapi.builder.IngredientBuilder;
import com.github.italomded.recipesapi.builder.RecipeBuilder;
import com.github.italomded.recipesapi.domain.recipe.Ingredient;
import com.github.italomded.recipesapi.domain.recipe.Measure;
import com.github.italomded.recipesapi.domain.recipe.Recipe;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.dto.form.*;
import com.github.italomded.recipesapi.repository.*;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.*;

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
    private AutoCloseable closeable;

    @Captor
    ArgumentCaptor<Recipe> captor;

    @BeforeEach
    public void createMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should successfully create the recipe")
    void scenario1() {
        //given
        Ingredient ingredient = IngredientBuilder.builder().withId(1L).build();
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong())).thenReturn(ingredient);

        Recipe recipe = RecipeBuilder.builder().withId(1L).build();
        Mockito.when(recipeRepository.save(Mockito.any())).thenReturn(recipe);
        RecipeCreateForm form = createRecipeForm();

        //when
        recipeService.createRecipe(form, new ApplicationUser());

        //then
        Mockito.verify(recipeRepository, Mockito.atMost(2)).save(captor.capture());

        recipe = captor.getAllValues().get(0);
        Assertions.assertEquals(form.title(), recipe.getTitle());
        Assertions.assertEquals(form.description(), recipe.getDescription());

        recipe = captor.getAllValues().get(1);
        Assertions.assertEquals(1, recipe.getImages().size());
        Assertions.assertEquals(3, recipe.getIngredientsOfRecipe().size());
    }

    @Test
    @DisplayName("It should throw an exception if when creating a recipe the ingredient does not previously exist")
    void scenario2() {
        //given
        Mockito.when(recipeRepository.save(Mockito.any()))
                .thenReturn(RecipeBuilder.builder().withId(1L).build());
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        RecipeCreateForm recipeForm = createRecipeForm();

        //when,then
        Assertions.assertThrows(EntityNotFoundException.class, () -> recipeService.createRecipe(recipeForm, new ApplicationUser()));
    }

    @Test
    @DisplayName("Should edit a recipe successfully")
    void scenario3() {
        //given
        ApplicationUser user = ApplicationUserBuilder.builder().withId(1L).build();
        Recipe recipe = RecipeBuilder.builder().withId(1L).withTitle("old title").withDescription("old description").withCreatorUser(user).build();

        Mockito.when(recipeRepository.getReferenceById(recipe.getID())).thenReturn(recipe);
        RecipeEditForm form = new RecipeEditForm("new title", "new description");

        //when
        recipeService.editRecipe(recipe.getID(), form, recipe.getCreatorUser());

        //then
        Mockito.verify(recipeRepository).save(recipe);
        Assertions.assertEquals(recipe.getTitle(), form.title());
        Assertions.assertEquals(recipe.getDescription(), form.description());
    }

    @Test
    @DisplayName("Throw an exception when editing a recipe if its ID does not exist")
    void scenario4() {
        //given
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong())).thenThrow(EntityNotFoundException.class);

        //when,then
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> recipeService.editRecipe(1L, new RecipeEditForm("A", "B"), new ApplicationUser()));
    }

    @Test
    @DisplayName("Throw an exception when deleting a recipe if its ID does not exist")
    void scenario5() {
        //given
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        //when,then
        Assertions.assertThrows(EntityNotFoundException.class, () -> recipeService.deleteRecipe(Mockito.anyLong(), new ApplicationUser()));
    }

    @Test
    @DisplayName("The user who authored a recipe must be able to delete it")
    void scenario6() {
        //given
        Recipe recipe = RecipeBuilder.builder().withCreatorUser(
                ApplicationUserBuilder.builder().withId(1L).build()
        ).build();

        Mockito.when(recipeRepository.getReferenceById(recipe.getID()))
                .thenReturn(recipe);

        //when
        recipeService.deleteRecipe(recipe.getID(), recipe.getCreatorUser());

        //then
        Mockito.verify(recipeRepository).delete(recipe);
    }

    @Test
    @DisplayName("Must verify that the user is the author of the recipe")
    void scenario7() {
        //given
        ApplicationUser userA = ApplicationUserBuilder.builder().withId(1L).build();
        ApplicationUser userB = ApplicationUserBuilder.builder().withId(2L).build();
        Recipe recipe = RecipeBuilder.builder().withCreatorUser(userA).build();

        //when,then
        Assertions.assertTrue(recipeService.verifyIfIsTheAuthor(recipe, userA));
        Assertions.assertThrows(BusinessRuleException.class, () -> recipeService.verifyIfIsTheAuthor(recipe, userB));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }

    private RecipeCreateForm createRecipeForm() {
        ImageForm[] images = {
                new ImageForm("www.someimages.com/niceimage"),
                new ImageForm("www.someimages.com/niceimage"),
                new ImageForm("www.someimages.com/niceimage")
        };
        RecipeIngredientCreateForm[] recipeIngredients = {
                new RecipeIngredientCreateForm(50.0, Measure.G, "Do this and do that", 5, 1L),
                new RecipeIngredientCreateForm(2.0, Measure.KG, "Do this and do that", 5, 2L),
                new RecipeIngredientCreateForm(20.0, Measure.MG, "Do this and do that", 5, 3L)
        };
        return new RecipeCreateForm(images, "Simple title", "Simple description", recipeIngredients);
    }
}
