package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.domain.Image;
import com.github.italomded.recipesapi.domain.Recipe;
import com.github.italomded.recipesapi.dto.form.ImageForm;
import com.github.italomded.recipesapi.repository.ImageRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import com.github.italomded.recipesapi.service.exception.EntityDoesNotExistException;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.lang.reflect.Field;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private ImageService imageService;

    private AutoCloseable closeable;
    private Field imageID;

    @Captor
    private ArgumentCaptor<Image> captor;

    @BeforeAll
    public void createFieldIdForRecipeIngredient() throws NoSuchFieldException {
        imageID = Image.class.getDeclaredField("ID");
        imageID.setAccessible(true);
    }

    @BeforeEach
    public void createMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldEditAImage() throws IllegalAccessException {
        Image image = new Image();
        imageID.set(image, 1L);

        Mockito.when(imageRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(image));

        ImageForm form = new ImageForm("0x12345".getBytes());
        imageService.editImage(1L, form);

        Mockito.verify(imageRepository).save(Mockito.any());
        Assertions.assertEquals(form.bytes(), image.getImageBytes());
    }

    @Test
    void shouldThrowAExceptionIfImageIdDoesntExistsOnEditImage() {
        Mockito.when(imageRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        ImageForm form = new ImageForm("0x12345".getBytes());
        Assertions.assertThrows(EntityDoesNotExistException.class, () -> imageService.editImage(1L, form));
    }

    @Test
    void shouldCreateAImage() throws IllegalAccessException {
        Recipe recipe = new Recipe();

        Image image = new Image();
        imageID.set(image, 1L);

        Mockito.when(recipeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(recipe));
        Mockito.when(imageRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(image));
        Mockito.when(imageRepository.save(Mockito.any()))
                .thenReturn(image);

        ImageForm form = new ImageForm("0x12345".getBytes());
        imageService.createImage(1L, form);

        Mockito.verify(imageRepository).save(captor.capture());
        Image imageCaptured = captor.getValue();

        Assertions.assertEquals(form.bytes(), imageCaptured.getImageBytes());
    }

    @Test
    void shouldThrowAExceptionIfRecipeIdDoesntExistsOnCreateImage() {
        Mockito.when(recipeRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        ImageForm form = new ImageForm("0x12345".getBytes());
        Assertions.assertThrows(EntityDoesNotExistException.class, () -> imageService.editImage(1L, form));
    }

    @Test
    void shouldDeleteAImage() {
        Recipe recipe = new Recipe();
        Image image = new Image("0x12345".getBytes(), recipe);
        recipe.addImage(image);

        Mockito.when(imageRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(image));

        imageService.deleteImage(1L);

        Mockito.verify(imageRepository).delete(Mockito.any());
        Mockito.verify(recipeRepository).save(Mockito.any());
        Assertions.assertEquals(0, recipe.getImages().size());
    }

    @Test
    void shouldThrowAExceptionIfImageIdDoesntExistsOnDeleteImage() {
        Mockito.when(imageRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntityDoesNotExistException.class, () -> imageService.deleteImage(1L));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }
}
