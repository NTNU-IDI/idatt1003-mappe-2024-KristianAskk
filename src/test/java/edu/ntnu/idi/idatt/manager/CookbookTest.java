package edu.ntnu.idi.idatt.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test class for the {@link Cookbook} class.
 *
 * <p>This class follows a nested test structure to separate positive and negative scenarios.
 * Each test verifies either a successful operation or ensures that invalid usage throws appropriate
 * exceptions.</p>
 *
 * @author Kristian Ask Selmer
 */
@DisplayName("Tests for the Cookbook class")
class CookbookTest {

  /**
   * Positive test scenarios for the Cookbook class, verifying correct behavior under valid
   * conditions.
   */
  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    private Cookbook cookbook;

    @BeforeEach
    void setUp() {
      cookbook = new Cookbook();
    }

    @Test
    @DisplayName("Should add a new recipe and retrieve it successfully")
    void testAddAndGetRecipe() {
      Recipe pasta = new Recipe("Pasta", "Simple pasta dish", "Boil pasta and serve", 2);
      cookbook.addRecipe(pasta);

      Recipe retrieved = cookbook.getRecipe("Pasta");
      assertNotNull(retrieved, "Recipe should be retrieved after adding");
      assertEquals("Pasta", retrieved.getName(), "Recipe name should match");
    }

    @Test
    @DisplayName("Should overwrite an existing recipe with the same name")
    void testAddRecipeOverwritesExisting() {
      Recipe chili = new Recipe("Chili", "Spicy stew", "Cook beans, add chili", 4);
      cookbook.addRecipe(chili);

      Recipe updatedChili = new Recipe("Chili", "Even spicier stew", "Add more chili peppers", 4);
      cookbook.addRecipe(updatedChili);

      Recipe retrieved = cookbook.getRecipe("Chili");
      assertNotNull(retrieved, "Updated Chili recipe should exist");
      assertEquals("Even spicier stew", retrieved.getDescription(),
          "Should reflect updated recipe details");
    }

    @Test
    @DisplayName("Should remove a recipe by name and return it")
    void testRemoveRecipe() {
      Recipe soup = new Recipe("Soup", "soup", "Simmer and serve", 3);
      cookbook.addRecipe(soup);

      Recipe removed = cookbook.removeRecipe("Soup");
      assertNotNull(removed, "Removed recipe should be returned");
      assertNull(cookbook.getRecipe("Soup"), "Recipe should be removed from the cookbook");
    }

    @Test
    @DisplayName("Should return a list of all recipe names")
    void testGetAllRecipeNames() {
      cookbook.addRecipe(new Recipe("Salad", "salad", "Chop", 2));
      cookbook.addRecipe(new Recipe("Cake", "dessert", "Bake", 8));

      List<String> names = cookbook.getAllRecipeNames();
      assertEquals(2, names.size(), "Should have two recipe names");
      assertTrue(names.contains("Salad"), "Salad should be in the list");
      assertTrue(names.contains("Cake"), "Cake should be in the list");
    }

    @Test
    @DisplayName("Should search recipes by keyword in their name")
    void testSearchRecipes() {
      cookbook.addRecipe(new Recipe("Tomato Soup", "soup", "Boil tomatoes", 3));
      cookbook.addRecipe(new Recipe("Tomato Salad", "salad", "Mix fresh tomatoes", 2));
      cookbook.addRecipe(
          new Recipe("Grilled Cheese", "Cheesy sandwich", "Grill cheese between bread", 1));

      List<Recipe> tomatoRecipes = cookbook.searchRecipes("tomato");
      assertEquals(2, tomatoRecipes.size(), "Should find two recipes containing 'tomato'");
    }

    @Test
    @DisplayName("Should suggest recipes that can be prepared from the given FoodStorage")
    void testSuggestRecipes() {

      FoodStorage foodStorage = new FoodStorage();
      Ingredient flour = new Ingredient("Flour", 2.0, "kg", 40.0, LocalDate.now().plusDays(7));
      Ingredient eggs = new Ingredient("Eggs", 12.0, "pcs", 30.0, LocalDate.now().plusDays(3));
      Ingredient milk = new Ingredient("Milk", 1.0, "l", 20.0, LocalDate.now().plusDays(5));
      foodStorage.addIngredient(flour);
      foodStorage.addIngredient(eggs);
      foodStorage.addIngredient(milk);

      Recipe pancakes = new Recipe("Pancakes", "Breakfast food", "Mix and fry", 2);
      pancakes.addIngredient(new Ingredient("Flour", 1.0, "kg", 20.0, LocalDate.now().plusDays(7)));
      pancakes.addIngredient(new Ingredient("Eggs", 6.0, "pcs", 15.0, LocalDate.now().plusDays(3)));
      Recipe souffle = new Recipe("Souffle", "dessert", "Whisk and bake", 2);
      souffle.addIngredient(new Ingredient("Eggs", 10.0, "pcs", 25.0, LocalDate.now().plusDays(3)));
      souffle.addIngredient(new Ingredient("Milk", 2.0, "l", 40.0, LocalDate.now().plusDays(5)));

      cookbook.addRecipe(pancakes);
      cookbook.addRecipe(souffle);

      List<Recipe> suggestions = cookbook.suggestRecipes(foodStorage);
      assertEquals(1, suggestions.size(), "Only Pancakes should be suggested");
      assertEquals("Pancakes", suggestions.get(0).getName(),
          "Pancakes should be the suggested recipe");
    }
  }

  /**
   * Negative test scenarios for the Cookbook class, ensuring that invalid inputs or operations
   * result in the expected exceptions.
   */
  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {

    private Cookbook cookbook;

    @BeforeEach
    void setUp() {
      cookbook = new Cookbook();
    }

    @Test
    @DisplayName("Should throw exception when adding a null recipe")
    void testAddNullRecipe() {
      assertThrows(IllegalArgumentException.class, () -> cookbook.addRecipe(null),
          "Adding a null recipe should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("Should throw exception when retrieving a recipe with null or blank name")
    void testGetRecipeWithInvalidName() {
      assertThrows(IllegalArgumentException.class, () -> cookbook.getRecipe(null),
          "Null name should throw IllegalArgumentException");
      assertThrows(IllegalArgumentException.class, () -> cookbook.getRecipe("   "),
          "Blank name should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("Should not find a recipe that does not exist")
    void testGetNonExistentRecipe() {
      assertNull(cookbook.getRecipe("None"),
          "Getting non-existent recipe should return null, not throw exception");
    }

    @Test
    @DisplayName("Should throw exception when removing a recipe with null or blank name")
    void testRemoveRecipeWithInvalidName() {
      assertThrows(IllegalArgumentException.class, () -> cookbook.removeRecipe(null),
          "Null name for removal should throw IllegalArgumentException");
      assertThrows(IllegalArgumentException.class, () -> cookbook.removeRecipe(""),
          "Empty name for removal should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("Should handle search with null or blank keyword by returning empty list")
    void testSearchWithNullOrBlankKeyword() {
      assertTrue(cookbook.searchRecipes(null).isEmpty(), "Null keyword should return empty list");
      assertTrue(cookbook.searchRecipes("  ").isEmpty(), "Blank keyword should return empty list");
    }

    @Test
    @DisplayName("Should throw exception when suggesting recipes with null FoodStorage")
    void testSuggestRecipesWithNullFoodStorage() {
      assertThrows(IllegalArgumentException.class, () -> cookbook.suggestRecipes(null),
          "Null FoodStorage should throw IllegalArgumentException");
    }
  }
}