package com.github.italomded.recipesapi.service;

import com.github.italomded.recipesapi.builder.ApplicationUserBuilder;
import com.github.italomded.recipesapi.builder.ImageBuilder;
import com.github.italomded.recipesapi.builder.RecipeBuilder;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.domain.recipe.Image;
import com.github.italomded.recipesapi.domain.recipe.Recipe;
import com.github.italomded.recipesapi.dto.form.ImageForm;
import com.github.italomded.recipesapi.repository.ImageRepository;
import com.github.italomded.recipesapi.repository.RecipeRepository;
import com.github.italomded.recipesapi.service.exception.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.*;

public class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private ImageService imageService;
    @Captor
    private ArgumentCaptor<Image> captor;

    private ImageBuilder imageBuilder = new ImageBuilder();
    private RecipeBuilder recipeBuilder = new RecipeBuilder();
    private ApplicationUserBuilder applicationUserBuilder = new ApplicationUserBuilder();
    private AutoCloseable closeable;

    @BeforeEach
    public void createMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should successfully edit an image")
    void scenario1() {
        //given
        ApplicationUser applicationUser = applicationUserBuilder.create().withId(1L).build();
        Recipe recipe = recipeBuilder.create().withId(1L).withCreatorUser(applicationUser).build();
        Image image = imageBuilder.create().withId(1L).withLink("www.someimages.com/anotherImage").withRecipe(recipe).build();

        Mockito.when(imageRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(image);

        ImageForm form = createRecipeForm();

        //when
        imageService.editImage(1L, form, image.getRecipe().getCreatorUser());

        //then
        Mockito.verify(imageRepository).save(Mockito.any());
        Assertions.assertEquals(form.link(), image.getLink());
    }

    @Test
    @DisplayName("Should throw an exception if image id does not exist when editing image")
    void scenario2() {
        //given
        ApplicationUser applicationUser = applicationUserBuilder.create().withId(1L).build();
        ImageForm form = createRecipeForm();

        //when
        Mockito.when(imageRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        //then
        Assertions.assertThrows(EntityNotFoundException.class, () -> imageService.editImage(1L, form, applicationUser));
    }

    @Test
    @DisplayName("Should successfully create an image")
    void scenario3() {
        //given
        ApplicationUser applicationUser = applicationUserBuilder.create().withId(1L).build();
        Recipe recipe = recipeBuilder.create().withId(1L).withCreatorUser(applicationUser).build();

        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(recipe);
        Mockito.when(imageRepository.save(Mockito.any()))
                .thenReturn(new Image());

        ImageForm form = createRecipeForm();

        //when
        imageService.createImage(1L, form, recipe.getCreatorUser());

        //then
        Mockito.verify(imageRepository).save(captor.capture());
        Image imageCaptured = captor.getValue();
        Assertions.assertEquals(form.link(), imageCaptured.getLink());
        Assertions.assertEquals(recipe, imageCaptured.getRecipe());
    }

    @Test
    @DisplayName("It should throw an exception if the recipe does not exist when creating an image")
    void scenario4() {
        //given
        ApplicationUser applicationUser = applicationUserBuilder.create().withId(1L).build();
        ImageForm form = createRecipeForm();

        //when
        Mockito.when(recipeRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        //then
        Assertions.assertThrows(EntityNotFoundException.class, () -> imageService.createImage(1L, form, applicationUser));
    }

    @Test
    @DisplayName("Should delete an image if there are at least two in the recipe")
    void scenario5() {
        //given
        ApplicationUser applicationUser = applicationUserBuilder.create().withId(1L).build();
        Recipe recipe = recipeBuilder.create().withId(1L).withCreatorUser(applicationUser).build();

        Image image1 = imageBuilder.create().withRecipe(recipe).withId(1L).build();
        Image image2 = imageBuilder.create().withRecipe(recipe).withId(2L).build();

        recipe.addImage(image1);
        recipe.addImage(image2);

        Mockito.when(imageRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(image1);

        //when
        imageService.deleteImage(1L, recipe.getCreatorUser());

        //then
        Mockito.verify(imageRepository).delete(image1);
        Mockito.verify(recipeRepository).save(recipe);
        Assertions.assertEquals(1, recipe.getImages().size());
    }

    @Test
    @DisplayName("Should throw an exception when deleting an image if the recipe only has it")
    void scenario6() {
        //given
        ApplicationUser applicationUser = applicationUserBuilder.create().withId(1L).build();
        Recipe recipe = recipeBuilder.create().withId(1L).withCreatorUser(applicationUser).build();

        Image image = imageBuilder.create().withId(1L).withRecipe(recipe).build();
        recipe.addImage(image);

        Mockito.when(imageRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(image);

        //when,then
        Assertions.assertThrows(BusinessRuleException.class, () -> imageService.deleteImage(1L, recipe.getCreatorUser()));
    }

    @Test
    @DisplayName("Should throw a exception if image id doesn't exists on delete image")
    void scenario7() {
        //given
        Mockito.when(imageRepository.getReferenceById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        //when,then
        Assertions.assertThrows(EntityNotFoundException.class, () -> imageService.deleteImage(1L, new ApplicationUser()));
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }

    private ImageForm createRecipeForm() {
        return new ImageForm("www.someimages.com/niceimage");
    }
}
