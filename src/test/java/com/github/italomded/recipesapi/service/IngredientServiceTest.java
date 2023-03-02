package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.builder.IngredientBuilder;
import com.github.italomded.recipesapi.domain.recipe.Ingredient;
import com.github.italomded.recipesapi.domain.recipe.TypeOfIngredient;
import com.github.italomded.recipesapi.dto.form.IngredientForm;
import com.github.italomded.recipesapi.repository.IngredientRepository;
import com.github.italomded.recipesapi.service.exception.DataValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.*;

public class IngredientServiceTest {
    @Mock
    private IngredientRepository ingredientRepository;
    @InjectMocks
    private IngredientService ingredientService;
    private AutoCloseable closeable;

    @Captor
    private ArgumentCaptor<Ingredient> captor;

    @BeforeEach
    public void createMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should successfully create a ingredient")
    void scenario1() {
        //given
        Mockito.when(ingredientRepository.save(Mockito.any()))
                .thenReturn(new Ingredient());
        Mockito.when(ingredientRepository.existsByName(Mockito.any()))
                .thenReturn(false);
        IngredientForm form = createIngredientForm();

        //when
        ingredientService.createIngredient(form);

        //then
        Mockito.verify(ingredientRepository).save(captor.capture());
        Ingredient ingredientCaptured = captor.getValue();

        Assertions.assertEquals(form.name(), ingredientCaptured.getName());
        Assertions.assertEquals(form.type(), ingredientCaptured.getCategory());
    }

    @Test
    @DisplayName("Should throw an exception when trying to create an ingredient whose name is not unique")
    void scenario2() {
        //given
        IngredientForm form = createIngredientForm();
        Mockito.when(ingredientRepository.existsByName(Mockito.any()))
                .thenReturn(true);

        //when,then
        Assertions.assertThrows(DataValidationException.class, () -> ingredientService.createIngredient(form));
    }

    @Test
    @DisplayName("Should successfully edit an ingredient")
    void scenario3() {
        //given
        Ingredient ingredient = IngredientBuilder.builder().withId(1L).withName("bread").build();
        IngredientForm form = createIngredientForm();

        Mockito.when(ingredientRepository.getReferenceById(ingredient.getID()))
                .thenReturn(ingredient);
        Mockito.when(ingredientRepository.save(Mockito.any()))
                .thenReturn(ingredient);

        //when
        ingredientService.editIngredient(ingredient.getID(), form);

        //then
        Mockito.verify(ingredientRepository).save(Mockito.any());
        Assertions.assertEquals(form.name(), ingredient.getName());
        Assertions.assertEquals(form.type(), ingredient.getCategory());
    }

    @Test
    @DisplayName("Should throw exception when editing ingredient if new name is not unique")
    void scenario4() {
        //given
        Ingredient ingredient = IngredientBuilder.builder().withId(1L).withName("bread").build();
        IngredientForm form = createIngredientForm();

        Mockito.when(ingredientRepository.getReferenceById(ingredient.getID()))
                .thenReturn(ingredient);
        Mockito.when(ingredientRepository.existsByName(form.name()))
                .thenReturn(true);

        //when,then
        Assertions.assertThrows(DataValidationException.class,
                () -> ingredientService.editIngredient(ingredient.getID(), form));
    }

    @Test
    @DisplayName("Should successfully delete an image")
    void scenario5() {
        //given
        Ingredient ingredient = IngredientBuilder.builder().withId(1L).withName("bread").build();
        Mockito.when(ingredientRepository.getReferenceById(ingredient.getID()))
                .thenReturn(ingredient);

        //when
        ingredientService.deleteIngredient(ingredient.getID());

        //then
        Mockito.verify(ingredientRepository).delete(ingredient);
    }

    @Test
    @DisplayName("Should throw an exception when trying to delete an ingredient whose ID does not exist")
    void scenario6() {
        //given
        Mockito.when(ingredientRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        //when,then
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> ingredientService.deleteIngredient(1L));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }

    private IngredientForm createIngredientForm() {
        return new IngredientForm("apple", TypeOfIngredient.FRUITS);
    }
}
