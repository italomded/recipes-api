package com.github.italomded.recipesapi.service;

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
    void shouldCreateAIngredient() {
        IngredientForm form = new IngredientForm("apple", TypeOfIngredient.FRUITS);
        Ingredient ingredient = new Ingredient();

        Mockito.when(ingredientRepository.save(Mockito.any()))
                .thenReturn(ingredient);
        Mockito.when(ingredientRepository.existsByName(Mockito.any()))
                .thenReturn(false);

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

        Mockito.when(ingredientRepository.existsByName(Mockito.any()))
                .thenReturn(true);

        Assertions.assertThrows(DataValidationException.class, () -> ingredientService.createIngredient(form));
    }

    @Test
    void shouldEditAIngredient() {
        Ingredient ingredient = new Ingredient("bread", TypeOfIngredient.CARBOHYDRATES);
        IngredientForm form = new IngredientForm("apple", TypeOfIngredient.FRUITS);

        Mockito.when(ingredientRepository.getReferenceById(Mockito.any()))
                .thenReturn(ingredient);
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

        Mockito.when(ingredientRepository.getReferenceById(Mockito.any()))
                .thenReturn(ingredient);
        Mockito.when(ingredientRepository.existsByName(Mockito.any()))
                .thenReturn(true);

        Assertions.assertThrows(DataValidationException.class, () -> ingredientService.editIngredient(1L, form));
    }

    @Test
    void shouldDeleteAIngredient() {
        Ingredient ingredient = new Ingredient("bread", TypeOfIngredient.CARBOHYDRATES);

        Mockito.when(ingredientRepository.getReferenceById(Mockito.any()))
                .thenReturn(ingredient);

        ingredientService.deleteIngredient(1L);
        Mockito.verify(ingredientRepository).delete(Mockito.any());
    }

    @Test
    void shouldThrowAExceptionOnDeleteIngredientIfDoesntExists() {
        Mockito.when(ingredientRepository.getReferenceById(Mockito.any()))
                .thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class, () -> ingredientService.deleteIngredient(1L));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }
}
