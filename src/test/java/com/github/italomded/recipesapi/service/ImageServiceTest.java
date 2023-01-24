package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.ApplicationUser;
import com.github.italomded.recipesapi.domain.Image;
import com.github.italomded.recipesapi.domain.Recipe;
import com.github.italomded.recipesapi.dto.form.ImageForm;
import com.github.italomded.recipesapi.repository.ImageRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.lang.reflect.Field;
import java.util.Optional;

public class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private ImageService imageService;

    private AutoCloseable closeable;

    @Captor
    private ArgumentCaptor<Image> captor;

    @BeforeEach
    public void createMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldEditAImage() {
        Image image = new Image("0x12345".getBytes(), RecipeServiceTest.createRecipe());

        Mockito.when(imageRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(image);

        ImageForm form = new ImageForm("0x12345".getBytes());
        imageService.editImage(1L, form, image.getRecipe().getCreatorUser());

        Mockito.verify(imageRepository).save(Mockito.any());
        Assertions.assertEquals(form.bytes(), image.getImageBytes());
    }

    @Test
    void shouldThrowAExceptionIfImageIdDoesntExistsOnEditImage() {
        Mockito.when(imageRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        ImageForm form = new ImageForm("0x12345".getBytes());
        Assertions.assertThrows(EntityNotFoundException.class, () -> imageService.editImage(1L, form, new ApplicationUser()));
    }

    @Test
    void shouldCreateAImage() {
        Recipe recipe = RecipeServiceTest.createRecipe();
        Image image = new Image();

        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(recipe);
        Mockito.when(imageRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(image);
        Mockito.when(imageRepository.save(Mockito.any()))
                .thenReturn(image);

        ImageForm form = new ImageForm("0x12345".getBytes());
        imageService.createImage(1L, form, recipe.getCreatorUser());

        Mockito.verify(imageRepository).save(captor.capture());
        Image imageCaptured = captor.getValue();

        Assertions.assertEquals(form.bytes(), imageCaptured.getImageBytes());
    }

    @Test
    void shouldThrowAExceptionIfRecipeIdDoesntExistsOnCreateImage() {
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        ImageForm form = new ImageForm("0x12345".getBytes());
        Assertions.assertThrows(EntityNotFoundException.class, () -> imageService.createImage(1L, form, new ApplicationUser()));
    }

    @Test
    void shouldDeleteAImage() throws IllegalAccessException, NoSuchFieldException {
        Recipe recipe = RecipeServiceTest.createRecipe();
        Image image1 = new Image("0x12345".getBytes(), recipe);
        Image image2 = new Image("0x12345".getBytes(), recipe);
        Field id = Image.class.getDeclaredField("ID");
        id.setAccessible(true);
        id.set(image1, 1L);
        id.set(image2, 2L);

        recipe.addImage(image1);
        recipe.addImage(image2);

        Mockito.when(imageRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(image1);

        imageService.deleteImage(1L, recipe.getCreatorUser());

        Mockito.verify(imageRepository).delete(Mockito.any());
        Mockito.verify(recipeRepository).save(Mockito.any());
        Assertions.assertEquals(1, recipe.getImages().size());
    }

    @Test
    void shouldThrowAExceptionOnDeleteImageIfRecipeHaveLessThanFourImages() {
        Recipe recipe = RecipeServiceTest.createRecipe();
        Image image = new Image("0x12345".getBytes(), recipe);
        recipe.addImage(image);

        Mockito.when(imageRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(image);

        Assertions.assertThrows(BusinessRuleException.class, () -> imageService.deleteImage(1L, recipe.getCreatorUser()));
    }

    @Test
    void shouldThrowAExceptionIfImageIdDoesntExistsOnDeleteImage() {
        Mockito.when(imageRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class, () -> imageService.deleteImage(1L, new ApplicationUser()));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }
}
