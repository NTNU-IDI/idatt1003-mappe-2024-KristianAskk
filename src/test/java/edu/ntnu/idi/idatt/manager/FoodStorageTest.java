package edu.ntnu.idi.idatt.manager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
 * Test class for the {@link FoodStorage} class.
 *
 * <p> Each test ensures that the methods in {@link FoodStorage} behave correctly under expected
 * conditions, and that appropriate exceptions are thrown when encountering invalid input or
 * operations.</p>
 *
 * @author Kristian Ask Selmer
 */
@DisplayName("Tests for the FoodStorage class")
class FoodStorageTest {

  /**
   * Tests that confirm correct, expected behavior under valid conditions.
   */
  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    private FoodStorage foodStorage;

    @BeforeEach
    void setUp() {
      foodStorage = new FoodStorage();
    }

    @Test
    @DisplayName("Should add ingredients and merge them if they share same attributes")
    void testAddAndMergeIngredients() {
      Ingredient rice1 = new Ingredient("Rice", 2.0, "kg", 15.0, LocalDate.now().plusDays(10));
      foodStorage.addIngredient(rice1);

      assertTrue(foodStorage.getAllIngredients().contains(rice1), "Rice should be added");
      assertEquals(1, foodStorage.getIngredientTypeCount(), "One type of ingredient expected");

      Ingredient rice2 = new Ingredient("Rice", 1.0, "kg", 15.0, LocalDate.now().plusDays(10));
      foodStorage.addIngredient(rice2);

      List<Ingredient> riceList = foodStorage.searchIngredientsByName("rice");
      assertEquals(1, riceList.size(), "Should still have a single entry after merging");
      assertEquals(3.0, riceList.get(0).getAmount(), "Merged amount should be total of both");
    }

    @Test
    @DisplayName("Should remove a specified amount of an ingredient successfully")
    void testConsumeIngredient() {
      Ingredient milk = new Ingredient("Milk", 3.0, "l", 20.0, LocalDate.now().plusDays(5));
      foodStorage.addIngredient(milk);

      assertDoesNotThrow(() -> foodStorage.consumeIngredient("Milk", 1.5),
          "Consuming available amount should not throw exception");

      List<Ingredient> milkList = foodStorage.searchIngredientsByName("milk");
      assertEquals(1, milkList.size(), "Milk should still exist");
      assertEquals(1.5, milkList.get(0).getAmount(), "Remaining amount should be updated");
    }

    @Test
    @DisplayName("Should verify if a recipe can be prepared")
    void testCanPrepareRecipe() {
      Ingredient flour = new Ingredient("Flour", 2.0, "kg", 40.0, LocalDate.now().plusDays(7));
      Ingredient eggs = new Ingredient("Eggs", 12.0, "pcs", 30.0, LocalDate.now().plusDays(3));
      foodStorage.addIngredient(flour);
      foodStorage.addIngredient(eggs);

      Recipe pancakeRecipe = new Recipe("Pancakes", "Simple dish", "Mix and cook", 2);
      pancakeRecipe.addIngredient(
          new Ingredient("Flour", 1.0, "kg", 20.0, LocalDate.now().plusDays(7)));
      pancakeRecipe.addIngredient(
          new Ingredient("Eggs", 6.0, "pcs", 15.0, LocalDate.now().plusDays(3)));

      assertTrue(foodStorage.canPrepareRecipe(pancakeRecipe),
          "Should be able to prepare pancakes with the available ingredients");
    }

    @Test
    @DisplayName("Should calculate total value of all ingredients")
    void testCalculateTotalValue() {
      Ingredient sugar = new Ingredient("Sugar", 1.0, "kg", 35.0, LocalDate.now().plusDays(10));
      Ingredient oliveOil = new Ingredient("Olive Oil", 0.5, "l", 50.0,
          LocalDate.now().plusMonths(1));
      foodStorage.addIngredient(sugar);
      foodStorage.addIngredient(oliveOil);

      double expectedValue =
          (sugar.getPrice() * sugar.getAmount()) + (oliveOil.getPrice() * oliveOil.getAmount());
      double totalValue = foodStorage.calculateTotalValue();
      assertEquals(expectedValue, totalValue, 0.01,
          "Total value should match sum of ingredient values");
    }

    @Test
    @DisplayName("Should handle preparing a recipe by consuming required ingredients")
    void testPrepareRecipe() {
      Ingredient tomato = new Ingredient("Tomato", 4.0, "pcs", 16.0, LocalDate.now().plusDays(4));
      Ingredient salt = new Ingredient("Salt", 100.0, "g", 5.0, LocalDate.now().plusDays(30));
      foodStorage.addIngredient(tomato);
      foodStorage.addIngredient(salt);

      Recipe salsa = new Recipe("Salsa", "Fresh dip", "Chop and mix", 1);
      salsa.addIngredient(new Ingredient("Tomato", 2.0, "pcs", 8.0, LocalDate.now().plusDays(4)));
      salsa.addIngredient(new Ingredient("Salt", 10.0, "g", 0.5, LocalDate.now().plusDays(30)));

      assertDoesNotThrow(() -> foodStorage.prepareRecipe(salsa),
          "Should prepare salsa without exception");
      assertEquals(2.0, foodStorage.searchIngredientsByName("tomato").get(0).getAmount(),
          "Tomato reduced correctly");
      assertEquals(90.0, foodStorage.searchIngredientsByName("salt").get(0).getAmount(),
          "Salt reduced correctly");
    }
  }

  /**
   * Tests that confirm proper exception handling and error reporting under invalid conditions.
   */
  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {

    private FoodStorage foodStorage;

    @BeforeEach
    void setUp() {
      foodStorage = new FoodStorage();
    }

    @Test
    @DisplayName("Should throw exception when adding a null ingredient")
    void testAddNullIngredient() {
      assertThrows(IllegalArgumentException.class, () -> foodStorage.addIngredient(null),
          "Adding null ingredient should fail");
    }

    @Test
    @DisplayName("Should throw exception when consuming ingredient that does not exist")
    void testConsumeNonExistentIngredient() {
      assertThrows(IllegalArgumentException.class,
          () -> foodStorage.consumeIngredient("NonExistent", 1.0),
          "Consuming a non-existent ingredient should fail");
    }

    @Test
    @DisplayName("Should throw exception when consuming negative amount")
    void testConsumeNegativeAmount() {
      Ingredient onions = new Ingredient("Onions", 2.0, "pcs", 4.0, LocalDate.now().plusDays(3));
      foodStorage.addIngredient(onions);
      assertThrows(IllegalArgumentException.class,
          () -> foodStorage.consumeIngredient("Onions", -2.0),
          "Consuming negative amount should fail");
    }

    @Test
    @DisplayName("Should throw exception if not enough non-expired ingredients are available")
    void testConsumeMoreThanAvailable() {
      Ingredient spinach = new Ingredient("Spinach", 100.0, "g", 5.0, LocalDate.now().plusDays(2));
      foodStorage.addIngredient(spinach);

      assertThrows(IllegalArgumentException.class,
          () -> foodStorage.consumeIngredient("Spinach", 200.0),
          "Consuming more than available should fail");
    }

    @Test
    @DisplayName("Should return empty list on null or blank search keyword, no exceptions")
    void testSearchWithNullOrBlankKeyword() {
      List<Ingredient> nullSearch = foodStorage.searchIngredientsByName(null);
      List<Ingredient> blankSearch = foodStorage.searchIngredientsByName("   ");

      assertNotNull(nullSearch, "Null keyword returns an empty list, not null");
      assertNotNull(blankSearch, "Blank keyword returns an empty list, not null");
      assertTrue(nullSearch.isEmpty(), "Null keyword should result in empty list");
      assertTrue(blankSearch.isEmpty(), "Blank keyword should result in empty list");
    }

    @Test
    @DisplayName("Should throw exception if recipe is null when checking canPrepareRecipe")
    void testNullRecipeInCanPrepareRecipe() {
      assertThrows(IllegalArgumentException.class, () -> foodStorage.canPrepareRecipe(null),
          "Null recipe should throw exception");
    }

    @Test
    @DisplayName("Should throw exception if recipe cannot be prepared due to insufficient ingredients")
    void testPrepareRecipeWithInsufficientIngredients() {
      Recipe gourmetDish = new Recipe("GourmetDish", "Fancy food", "Cook", 2);
      gourmetDish.addIngredient(
          new Ingredient("Caviar", 50.0, "g", 100.0, LocalDate.now().plusDays(1)));

      assertThrows(IllegalArgumentException.class, () -> foodStorage.prepareRecipe(gourmetDish),
          "Preparing a recipe without enough ingredients should fail");
    }
  }
}