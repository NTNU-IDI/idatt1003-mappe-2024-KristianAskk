package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Recipe class.
 *
 * <p>Tests scenarios for creating and managing a Recipe object.</p>
 *
 * @author Kristian Ask Selmer
 */
@DisplayName("Tests for the Recipe class")
class RecipeTest {

  private Recipe recipe;

  @BeforeEach
  void setUp() {
    recipe = new Recipe("Pasta Carbonara",
        "A classic Italian pasta dish with pancetta and eggs.",
        "Cook pasta, fry pancetta, mix with eggs and cheese.",
        4);
  }

  /**
   * Positive tests for valid recipe operations.
   */
  @Nested
  @DisplayName("Positive Test Cases")
  class PositiveTests {

    @Test
    @DisplayName("Should create a recipe with valid attributes")
    void testValidRecipeCreation() {
      assertEquals("Pasta Carbonara", recipe.getName());
      assertEquals("A classic Italian pasta dish with pancetta and eggs.", recipe.getDescription());
      assertEquals("Cook pasta, fry pancetta, mix with eggs and cheese.", recipe.getInstructions());
      assertEquals(4, recipe.getServings());
    }

    @Test
    @DisplayName("Should update recipe name successfully")
    void testSetName() {
      recipe.setName("Creamy Alfredo Pasta");
      assertEquals("Creamy Alfredo Pasta", recipe.getName());
    }

    @Test
    @DisplayName("Should update recipe description successfully")
    void testSetDescription() {
      recipe.setDescription("A creamy pasta dish made with butter and parmesan cheese.");
      assertEquals("A creamy pasta dish made with butter and parmesan cheese.",
          recipe.getDescription());
    }

    @Test
    @DisplayName("Should update recipe instructions successfully")
    void testSetInstructions() {
      recipe.setInstructions("Boil pasta, melt butter, add cream, cheese, and toss.");
      assertEquals("Boil pasta, melt butter, add cream, cheese, and toss.",
          recipe.getInstructions());
    }

    @Test
    @DisplayName("Should update recipe servings successfully")
    void testSetServings() {
      recipe.setServings(6);
      assertEquals(6, recipe.getServings());
    }

    @Test
    @DisplayName("Should add a new ingredient to the recipe")
    void testAddIngredient() {
      Ingredient cheese = new Ingredient("Parmesan", 100.0, "g", 20.0,
          java.time.LocalDate.now().plusDays(10));
      recipe.addIngredient(cheese);

      List<Ingredient> ingredients = recipe.getIngredients();
      assertTrue(ingredients.contains(cheese), "Recipe should contain the added cheese ingredient");
    }

    @Test
    @DisplayName("Should remove an existing ingredient from the recipe")
    void testRemoveIngredient() {
      Ingredient bacon = new Ingredient("Bacon", 50.0, "g", 10.0,
          java.time.LocalDate.now().plusDays(5));
      recipe.addIngredient(bacon);
      recipe.removeIngredient(bacon);

      assertFalse(recipe.getIngredients().contains(bacon),
          "Recipe should no longer contain the bacon ingredient");
    }

    @Test
    @DisplayName("Should produce a readable string representation of the recipe")
    void testToString() {
      Ingredient pasta = new Ingredient("Spaghetti", 200.0, "g", 5.0,
          java.time.LocalDate.now().plusDays(7));
      recipe.addIngredient(pasta);

      String recipeString = recipe.toString();
      assertTrue(recipeString.contains("Pasta Carbonara"), "Name should appear in toString output");
      assertTrue(recipeString.contains("A classic Italian pasta dish"),
          "Description should appear in toString output");
      assertTrue(recipeString.contains("Spaghetti"),
          "Added ingredient should appear in toString output");
    }

    @Test
    @DisplayName("Should handle toString call with no ingredients gracefully")
    void testToStringNoIngredients() {
      String recipeString = recipe.toString();
      assertTrue(recipeString.contains("Ingredients:"), "Should list ingredients even if empty");
    }
  }

  /**
   * Negative tests to ensure proper exceptions are thrown for invalid operations.
   */
  @Nested
  @DisplayName("Negative Test Cases")
  class NegativeTests {

    @Test
    @DisplayName("Should throw exception when creating recipe with null or empty name")
    void testInvalidNameOnCreation() {
      assertThrows(IllegalArgumentException.class,
          () -> new Recipe(null, "Some description", "Some instructions", 2));
      assertThrows(IllegalArgumentException.class,
          () -> new Recipe("", "Some description", "Some instructions", 2));
    }

    @Test
    @DisplayName("Should throw exception when creating recipe with null or empty description")
    void testInvalidDescriptionOnCreation() {
      assertThrows(IllegalArgumentException.class,
          () -> new Recipe("Pesto Pasta", null, "Some instructions", 2));
      assertThrows(IllegalArgumentException.class,
          () -> new Recipe("Pesto Pasta", "", "Some instructions", 2));
    }

    @Test
    @DisplayName("Should throw exception when creating recipe with null or empty instructions")
    void testInvalidInstructionsOnCreation() {
      assertThrows(IllegalArgumentException.class,
          () -> new Recipe("Pesto Pasta", "Green sauce dish", null, 2));
      assertThrows(IllegalArgumentException.class,
          () -> new Recipe("Pesto Pasta", "Green sauce dish", "", 2));
    }

    @Test
    @DisplayName("Should throw exception when creating recipe with zero or negative servings")
    void testInvalidServingsOnCreation() {
      assertThrows(IllegalArgumentException.class,
          () -> new Recipe("Pesto Pasta", "Green sauce dish", "Boil pasta", 0));
      assertThrows(IllegalArgumentException.class,
          () -> new Recipe("Pesto Pasta", "Green sauce dish", "Boil pasta", -1));
    }

    @Test
    @DisplayName("Should throw exception when setting name to null or empty")
    void testSetNameInvalid() {
      assertThrows(IllegalArgumentException.class, () -> recipe.setName(null));
      assertThrows(IllegalArgumentException.class, () -> recipe.setName(""));
    }

    @Test
    @DisplayName("Should throw exception when setting description to null or empty")
    void testSetDescriptionInvalid() {
      assertThrows(IllegalArgumentException.class, () -> recipe.setDescription(null));
      assertThrows(IllegalArgumentException.class, () -> recipe.setDescription(""));
    }

    @Test
    @DisplayName("Should throw exception when setting instructions to null or empty")
    void testSetInstructionsInvalid() {
      assertThrows(IllegalArgumentException.class, () -> recipe.setInstructions(null));
      assertThrows(IllegalArgumentException.class, () -> recipe.setInstructions(""));
    }

    @Test
    @DisplayName("Should throw exception when setting servings to zero or negative")
    void testSetServingsInvalid() {
      assertThrows(IllegalArgumentException.class, () -> recipe.setServings(0));
      assertThrows(IllegalArgumentException.class, () -> recipe.setServings(-5));
    }

    @Test
    @DisplayName("Should throw exception when adding a null ingredient")
    void testAddNullIngredient() {
      assertThrows(IllegalArgumentException.class, () -> recipe.addIngredient(null));
    }

    @Test
    @DisplayName("Should throw exception when removing a null ingredient")
    void testRemoveNullIngredient() {
      assertThrows(IllegalArgumentException.class, () -> recipe.removeIngredient(null));
    }

    @Test
    @DisplayName("Should throw exception when removing an ingredient that does not exist")
    void testRemoveNonExistentIngredient() {
      Ingredient missing = new Ingredient("Nonexistent", 50.0, "g", 10.0,
          java.time.LocalDate.now().plusDays(3));
      assertThrows(IllegalArgumentException.class, () -> recipe.removeIngredient(missing));
    }
  }
}