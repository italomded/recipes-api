package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.Ingredient;
import com.github.italomded.recipesapi.domain.TypeOfIngredient;
import com.github.italomded.recipesapi.dto.form.IngredientForm;
import com.github.italomded.recipesapi.repository.IngredientRepository;
import com.github.italomded.recipesapi.service.exception.DataValidationException;
import com.github.italomded.recipesapi.service.exception.EntityDoesNotExistException;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.lang.reflect.Field;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IngredientServiceTest {
    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    private AutoCloseable closeable;
    private Field ingredientID;

    @Captor
    private ArgumentCaptor<Ingredient> captor;

    @BeforeAll
    public void createFieldIdForRecipeIngredient() throws NoSuchFieldException {
        ingredientID = Ingredient.class.getDeclaredField("ID");
        ingredientID.setAccessible(true);
    }

    @BeforeEach
    public void createMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAIngredient() throws IllegalAccessException {
        IngredientForm form = new IngredientForm("apple", TypeOfIngredient.FRUITS);
        Ingredient ingredient = new Ingredient();
        ingredientID.set(ingredient, 1L);

        Mockito.when(ingredientRepository.save(Mockito.any()))
                .thenReturn(ingredient);
        Mockito.when(ingredientRepository.findByName(Mockito.any()))
                .thenReturn(Optional.empty());

        ingredientService.createIngredient(form);

        Mockito.verify(ingredientRepository).save(captor.capture());
        Ingredient ingredientCaptured = captor.getValue();

        Assertions.assertEquals(form.name(), ingredientCaptured.getName());
        Assertions.assertEquals(form.type(), ingredientCaptured.getCategory());
    }

    @Test
    void shouldThrowAExceptionOnCreateIngredientIfNameIsNotUnique() {
        Ingredient ingredient = new Ingredient("apple", TypeOfIngredient.FRUITS);
        IngredientForm form = new IngredientForm("Apple", TypeOfIngredient.FRUITS);

        Mockito.when(ingredientRepository.findByName(Mockito.any()))
                .thenReturn(Optional.of(ingredient));

        Assertions.assertThrows(DataValidationException.class, () -> ingredientService.createIngredient(form));
    }

    @Test
    void shouldEditAIngredient() throws IllegalAccessException {
        Ingredient ingredient = new Ingredient("bread", TypeOfIngredient.CARBOHYDRATES);
        ingredientID.set(ingredient, 1L);
        IngredientForm form = new IngredientForm("apple", TypeOfIngredient.FRUITS);

        Mockito.when(ingredientRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(ingredient));
        Mockito.when(ingredientRepository.save(Mockito.any()))
                .thenReturn(ingredient);

        ingredientService.editIngredient(1L, form);

        Mockito.verify(ingredientRepository).save(Mockito.any());
        Assertions.assertEquals(form.name(), ingredient.getName());
        Assertions.assertEquals(form.type(), ingredient.getCategory());
    }

    @Test
    void shouldThrowAExceptionOnEditIngredientIfNameIsNotUnique() {
        Ingredient ingredient = new Ingredient("bread", TypeOfIngredient.CARBOHYDRATES);
        IngredientForm form = new IngredientForm("apple", TypeOfIngredient.FRUITS);

        Mockito.when(ingredientRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(ingredient));
        Mockito.when(ingredientRepository.findByName(Mockito.any()))
                .thenReturn(Optional.of(ingredient));

        Assertions.assertThrows(DataValidationException.class, () -> ingredientService.editIngredient(1L, form));
    }

    @Test
    void shouldDeleteAIngredient() {
        Ingredient ingredient = new Ingredient("bread", TypeOfIngredient.CARBOHYDRATES);

        Mockito.when(ingredientRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(ingredient));

        ingredientService.deleteIngredient(1L);
        Mockito.verify(ingredientRepository).delete(Mockito.any());
    }

    @Test
    void shouldThrowAExceptionOnDeleteIngredientIfDoesntExists() {
        Mockito.when(ingredientRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntityDoesNotExistException.class, () -> ingredientService.deleteIngredient(1L));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }
}
