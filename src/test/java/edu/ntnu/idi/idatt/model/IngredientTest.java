package edu.ntnu.idi.idatt.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
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
public class IngredientTest {

  /**
   * Tests that a valid Ingredient object is created with correct attributes.
   */
  @Test
  void testValidIngredientCreation() {
    LocalDate expirationDate = LocalDate.now().plusDays(5);
    Ingredient ingredient = new Ingredient("Sugar", 1.0, "kg", 10.0, expirationDate);

    assertEquals("Sugar", ingredient.getName());
    assertEquals(1.0, ingredient.getAmount());
    assertEquals("kg", ingredient.getUnit());
    assertEquals(10.0, ingredient.getPrice());
    assertEquals(expirationDate, ingredient.getExpirationDate());
  }

  /**
   * Tests that creating an Ingredient with an empty name throws an exception.
   */
  @Test
  void testIngredientWithEmptyName() {
    LocalDate expirationDate = LocalDate.now().plusDays(5);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("", 1.0, "kg", 10.0, expirationDate);
    });

    assertEquals("Name cannot be null or blank", exception.getMessage());
  }

  /**
   * Tests that creating an Ingredient with a negative amount throws an exception.
   */
  @Test
  void testIngredientWithNegativeAmount() {
    LocalDate expirationDate = LocalDate.now().plusDays(5);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("Sugar", -1.0, "kg", 10.0, expirationDate);
    });

    assertEquals("Amount cannot be negative", exception.getMessage());
  }

  /**
   * Tests that creating an Ingredient with an empty unit throws an exception.
   */
  @Test
  void testIngredientWithEmptyUnit() {
    LocalDate expirationDate = LocalDate.now().plusDays(5);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("Sugar", 1.0, "", 10.0, expirationDate);
    });

    assertEquals("Unit cannot be null or blank", exception.getMessage());
  }

  /**
   * Tests that creating an Ingredient with a negative price throws an exception.
   */
  @Test
  void testIngredientWithNegativePrice() {
    LocalDate expirationDate = LocalDate.now().plusDays(5);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("Sugar", 1.0, "kg", -10.0, expirationDate);
    });

    assertEquals("Price cannot be negative", exception.getMessage());
  }

  /**
   * Tests that creating an Ingredient with an expiration date in the past throws an exception.
   */
  @Test
  void testIngredientWithPastExpirationDate() {
    LocalDate expirationDate = LocalDate.now().minusDays(1);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("Sugar", 1.0, "kg", 10.0, expirationDate);
    });

    assertEquals("Expiration date cannot be in the past", exception.getMessage());
  }

  /**
   * Tests that setting a new valid amount updates the amount correctly.
   */
  @Test
  void testSetValidAmount() {
    Ingredient ingredient = new Ingredient("Sugar", 1.0, "kg", 10.0, LocalDate.now().plusDays(5));
    ingredient.setAmount(2.5);

    assertEquals(2.5, ingredient.getAmount());
  }

  /**
   * Tests that setting a negative amount throws an exception.
   */
  @Test
  void testSetNegativeAmount() {
    Ingredient ingredient = new Ingredient("Sugar", 1.0, "kg", 10.0, LocalDate.now().plusDays(5));

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      ingredient.setAmount(-2.0);
    });

    assertEquals("Amount cannot be negative", exception.getMessage());
  }

  /**
   * Tests that creating an Ingredient with an expiration date in the past throws an exception.
   */
  @Test
  void testIsExpired() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("Milk", 1.0, "liter", 15.0, LocalDate.now().minusDays(1));
    });

    assertEquals("Expiration date cannot be in the past", exception.getMessage());
  }

  /**
   * Tests the isExpired method for an ingredient that is not expired.
   */
  @Test
  void testIsNotExpired() {
    Ingredient ingredient = new Ingredient("Milk", 1.0, "liter", 15.0, LocalDate.now().plusDays(1));

    assertFalse(ingredient.isExpired());
  }

  /**
   * Tests that setting a valid expiration date updates the date correctly.
   */
  @Test
  void testSetValidExpirationDate() {
    Ingredient ingredient = new Ingredient("Eggs", 12.0, "units", 20.0,
        LocalDate.now().plusDays(10));
    LocalDate newExpirationDate = LocalDate.now().plusDays(20);
    ingredient.setExpirationDate(newExpirationDate);

    assertEquals(newExpirationDate, ingredient.getExpirationDate());
  }

  /**
   * Tests that setting an expiration date in the past throws an exception.
   */
  @Test
  void testSetPastExpirationDate() {
    Ingredient ingredient = new Ingredient("Eggs", 12.0, "units", 20.0,
        LocalDate.now().plusDays(10));

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      ingredient.setExpirationDate(LocalDate.now().minusDays(5));
    });

    assertEquals("Expiration date cannot be in the past", exception.getMessage());
  }

  /**
   * Tests that the toString method returns a properly formatted string.
   */
  @Test
  void testToString() {
    LocalDate expirationDate = LocalDate.of(2030, 12, 31);
    Ingredient ingredient = new Ingredient("Butter", 0.5, "kg", 40.0, expirationDate);
    String expectedString = "Ingredient{name='Butter', amount=0.50 kg, price=40.00, expirationDate=2030-12-31}";
    assertEquals(expectedString, ingredient.toString());
  }
}