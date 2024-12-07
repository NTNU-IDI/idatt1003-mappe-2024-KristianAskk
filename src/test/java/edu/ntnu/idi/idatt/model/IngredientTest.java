package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test class for the {@link Ingredient} class.
 *
 * <p>This class contains unit tests that verify the functionality of the
 * {@code Ingredient} class, including validation of constructor parameters, setter methods, and
 * other behaviors.</p>
 *
 * @author Kristian Ask Selmer
 */
@DisplayName("Tests for the Ingredient class")
class IngredientTest {

  /**
   * Positive tests, verifying correct and valid behavior of the Ingredient class.
   */
  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    private Ingredient ingredient;
    private LocalDate futureDate;

    @BeforeEach
    void setUp() {
      futureDate = LocalDate.now().plusDays(5);
      ingredient = new Ingredient("Sugar", 1.0, "kg", 10.0, futureDate);
    }

    @Test
    @DisplayName("Should create an ingredient with valid attributes")
    void testValidIngredientCreation() {
      assertEquals("Sugar", ingredient.getName(), "Name should match");
      assertEquals(1.0, ingredient.getAmount(), "Amount should match");
      assertEquals("kg", ingredient.getUnit(), "Unit should match");
      assertEquals(10.0, ingredient.getPrice(), "Price should match");
      assertEquals(futureDate, ingredient.getExpirationDate(), "Expiration date should match");
    }

    @Test
    @DisplayName("Should set a valid amount without errors")
    void testSetValidAmount() {
      ingredient.setAmount(2.5);
      assertEquals(2.5, ingredient.getAmount(), "Amount should be updated");
    }

    @Test
    @DisplayName("Should verify that an ingredient is not expired")
    void testIsNotExpired() {
      assertFalse(ingredient.isExpired(), "Ingredient should not be expired yet");
    }

    @Test
    @DisplayName("Should update expiration date to a valid future date")
    void testSetValidExpirationDate() {
      LocalDate newExpirationDate = LocalDate.now().plusDays(20);
      ingredient.setExpirationDate(newExpirationDate);
      assertEquals(newExpirationDate, ingredient.getExpirationDate(),
          "Expiration date should be updated");
    }

    @Test
    @DisplayName("Should return a properly formatted string representation")
    void testToString() {
      LocalDate distantFuture = LocalDate.of(2030, 12, 31);
      ingredient.setExpirationDate(distantFuture);

      String expectedString = "Ingredient{name='Sugar', amount=1.00 kg, price=10.00, expirationDate=2030-12-31}";
      assertEquals(expectedString, ingredient.toString(),
          "toString should return the expected format");
    }
  }

  /**
   * Negative tests, verifying that appropriate exceptions are thrown for invalid inputs or
   * scenarios.
   */
  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {

    @Test
    @DisplayName("Should throw exception when creating ingredient with empty name")
    void testIngredientWithEmptyName() {
      LocalDate expirationDate = LocalDate.now().plusDays(5);
      Exception exception = assertThrows(IllegalArgumentException.class, () ->
          new Ingredient("", 1.0, "kg", 10.0, expirationDate)
      );
      assertEquals("Name cannot be null or blank", exception.getMessage(),
          "Exception message should match");
    }

    @Test
    @DisplayName("Should throw exception when creating ingredient with negative amount")
    void testIngredientWithNegativeAmount() {
      LocalDate expirationDate = LocalDate.now().plusDays(5);
      Exception exception = assertThrows(IllegalArgumentException.class, () ->
          new Ingredient("Sugar", -1.0, "kg", 10.0, expirationDate)
      );
      assertEquals("Amount cannot be negative", exception.getMessage(),
          "Exception message should match");
    }

    @Test
    @DisplayName("Should throw exception when creating ingredient with empty unit")
    void testIngredientWithEmptyUnit() {
      LocalDate expirationDate = LocalDate.now().plusDays(5);
      Exception exception = assertThrows(IllegalArgumentException.class, () ->
          new Ingredient("Sugar", 1.0, "", 10.0, expirationDate)
      );
      assertEquals("Unit cannot be null or blank", exception.getMessage(),
          "Exception message should match");
    }

    @Test
    @DisplayName("Should throw exception when creating ingredient with negative price")
    void testIngredientWithNegativePrice() {
      LocalDate expirationDate = LocalDate.now().plusDays(5);
      Exception exception = assertThrows(IllegalArgumentException.class, () ->
          new Ingredient("Sugar", 1.0, "kg", -10.0, expirationDate)
      );
      assertEquals("Price cannot be negative", exception.getMessage(),
          "Exception message should match");
    }

    @Test
    @DisplayName("Should throw exception when creating ingredient with past expiration date")
    void testIngredientWithPastExpirationDate() {
      LocalDate pastDate = LocalDate.now().minusDays(1);
      Exception exception = assertThrows(IllegalArgumentException.class, () ->
          new Ingredient("Sugar", 1.0, "kg", 10.0, pastDate)
      );
      assertEquals("Expiration date cannot be in the past", exception.getMessage(),
          "Exception message should match");
    }

    @Test
    @DisplayName("Should throw exception when setting negative amount")
    void testSetNegativeAmount() {
      Ingredient ingredient = new Ingredient("Sugar", 1.0, "kg", 10.0, LocalDate.now().plusDays(5));

      Exception exception = assertThrows(IllegalArgumentException.class, () ->
          ingredient.setAmount(-2.0)
      );
      assertEquals("Amount cannot be negative", exception.getMessage(),
          "Exception message should match");
    }

    @Test
    @DisplayName("Should throw exception when creating ingredient with expired date to test isExpired logic")
    void testCreateIngredientWithExpiredDateThrows() {
      LocalDate expiredDate = LocalDate.now().minusDays(1);
      Exception exception = assertThrows(IllegalArgumentException.class, () ->
          new Ingredient("Milk", 1.0, "liter", 15.0, expiredDate)
      );
      assertEquals("Expiration date cannot be in the past", exception.getMessage(),
          "Exception message should match");
    }

    @Test
    @DisplayName("Should throw exception when setting an expiration date in the past")
    void testSetPastExpirationDate() {
      Ingredient ingredient = new Ingredient("Eggs", 12.0, "units", 20.0,
          LocalDate.now().plusDays(10));

      Exception exception = assertThrows(IllegalArgumentException.class, () ->
          ingredient.setExpirationDate(LocalDate.now().minusDays(5))
      );
      assertEquals("Expiration date cannot be in the past", exception.getMessage(),
          "Exception message should match");
    }
  }
}