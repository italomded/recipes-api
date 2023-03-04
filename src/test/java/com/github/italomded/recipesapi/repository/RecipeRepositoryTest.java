package com.github.italomded.recipesapi.repository;

import com.github.italomded.recipesapi.builder.ApplicationUserBuilder;
import com.github.italomded.recipesapi.builder.IngredientBuilder;
import com.github.italomded.recipesapi.builder.RecipeBuilder;
import com.github.italomded.recipesapi.builder.RecipeIngredientBuilder;
import com.github.italomded.recipesapi.domain.recipe.*;
import com.github.italomded.recipesapi.domain.user.ApplicationUser;
import com.github.italomded.recipesapi.domain.user.Role;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class RecipeRepositoryTest {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Should retrieve only one recipe that matches all passed ingredient id's")
    void scenario1() {
        //given
        var ingredientsId = Arrays.asList(1L, 2L, 3L);
        var pageable = PageRequest.ofSize(20);

        //when
        List<Recipe> recipes = recipeRepository.findByIngredients(ingredientsId.toArray(Long[]::new), pageable).getContent();

        //then
        Assertions.assertEquals(1, recipes.size());
        Recipe recipe = recipes.get(0);
        var ingredientsOfRecipe = recipe.getIngredientsOfRecipe();
        Assertions.assertEquals(3, ingredientsOfRecipe.size());
        verifyIfIngredientsMatch(ingredientsOfRecipe, ingredientsId);
    }

    @Test
    @DisplayName("Should retrieve two recipes that matches all passed ingredient id's")
    void scenario2() {
        //given
        var ingredientsId = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        var pageable = PageRequest.ofSize(20);

        //when
        List<Recipe> recipes = recipeRepository.findByIngredients(ingredientsId.toArray(Long[]::new), pageable).getContent();

        //then
        Assertions.assertEquals(2, recipes.size());
        recipes.forEach(recipe -> {
            var ingredientsOfRecipe = recipe.getIngredientsOfRecipe();
            verifyIfIngredientsMatch(ingredientsOfRecipe, ingredientsId);
        });
    }

    @Test
    @DisplayName("Should retrieve no recipes because don't they don't match with all ingredients id's")
    void scenario3() {
        //given
        var ingredientsId = Arrays.asList(1L);
        var pageable = PageRequest.ofSize(20);

        //when
        List<Recipe> recipes = recipeRepository.findByIngredients(ingredientsId.toArray(Long[]::new), pageable).getContent();

        //then
        Assertions.assertEquals(0, recipes.size());
    }

    private void verifyIfIngredientsMatch(List<RecipeIngredient> recipeIngredients, List<Long> ingredientsId) {
        recipeIngredients.forEach(recipeIngredient -> {
            boolean result = ingredientsId.contains(recipeIngredient.getIngredient().getID());
            Assertions.assertTrue(result);
        });
    }

    @BeforeEach
    void persist() {
        List<Ingredient> ingredientList = Arrays.asList(
                IngredientBuilder.builder().withName("apple").withCategory(TypeOfIngredient.FRUITS).build(),
                IngredientBuilder.builder().withName("pineapple").withCategory(TypeOfIngredient.FRUITS).build(),
                IngredientBuilder.builder().withName("avocado").withCategory(TypeOfIngredient.FRUITS).build(),
                IngredientBuilder.builder().withName("banana").withCategory(TypeOfIngredient.FRUITS).build(),
                IngredientBuilder.builder().withName("lemon").withCategory(TypeOfIngredient.FRUITS).build()
        );
        ingredientList.forEach(ingredient -> em.persist(ingredient));

        Role role = new Role("ROLE_USR");
        ApplicationUser user = ApplicationUserBuilder.builder().withUsername("user").withPassword("password").withRole(role).build();
        em.persist(role);
        em.persist(user);

        Recipe recipe = RecipeBuilder.builder().withTitle("Title").withDescription("Description").withCreatorUser(user).build();
        em.persist(recipe);
        Quantity quantity = new Quantity(20.0, Measure.KG);
        var builder = RecipeIngredientBuilder.builder()
                .withSequence(1).withPrepareMinutes(5).withQuantity(quantity).withRecipe(recipe).withInstruction("Do");

        em.persist(builder.withIngredient(ingredientList.get(0)).build());
        em.clear();
        em.persist(builder.withIngredient(ingredientList.get(1)).withId(null).build());
        em.clear();
        em.persist(builder.withIngredient(ingredientList.get(2)).withId(null).build());
        em.clear();
        em.persist(builder.withIngredient(ingredientList.get(3)).withId(null).build());
        em.clear();
        em.persist(builder.withIngredient(ingredientList.get(4)).withId(null).build());
        em.clear();

        Recipe anotherRecipe = RecipeBuilder.builder().withTitle("Other title").withDescription("Other description").withCreatorUser(user).build();
        em.persist(anotherRecipe);
        em.persist(builder.withIngredient(ingredientList.get(0)).withId(null).withRecipe(anotherRecipe).build());
        em.clear();
        em.persist(builder.withIngredient(ingredientList.get(1)).withId(null).build());
        em.clear();
        em.persist(builder.withIngredient(ingredientList.get(2)).withId(null).build());
    }
}
