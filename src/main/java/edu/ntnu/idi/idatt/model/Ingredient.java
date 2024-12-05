package edu.ntnu.idi.idatt.model;

import java.time.LocalDate;

/**
 * Represents an ingredient item with a name, amount, unit, price, and expiration date.
 *
 * <p>This class models a ingredient, tracking its details such as name,
 * amount, unit, price, and expiration date. It provides methods to access and modify the ingredient
 * item's information and ensures that provided values are valid.
 * </p>
 *
 * @author Kristian Ask Selmer
 */
public class Ingredient {

  private final String name;
  private double amount;
  private String unit;
  private double price;
  private LocalDate expirationDate;

  /**
   * Constructs a new {@code Ingredient} item with the specified name, amount, unit, price, and
   * expiration date.
   *
   * @param name           the name of the ingredient
   * @param amount         the amount of the ingredient
   * @param unit           the unit of measurement for the amount
   * @param price          the price of the ingredient
   * @param expirationDate the expiration date of the ingredient
   * @throws IllegalArgumentException if any of the arguments are invalid
   */
  public Ingredient(String name, double amount, String unit,
      double price, LocalDate expirationDate) {
    validateString(name, "Name");
    validateAmount(amount);
    validateString(unit, "Unit");
    validatePrice(price);
    validateExpirationDate(expirationDate);

    this.name = name;
    this.amount = amount;
    this.unit = unit;
    this.price = price;
    this.expirationDate = expirationDate;
  }

  /**
   * Returns the name of the ingredient item.
   *
   * @return the name of the ingredient item
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the amount of the ingredient item.
   *
   * @return the amount of the ingredient item
   */
  public double getAmount() {
    return amount;
  }

  /**
   * Sets the amount of the ingredient item.
   *
   * @param amount the new amount of the ingredient item
   * @throws IllegalArgumentException if amount is negative
   */
  public void setAmount(double amount) {
    validateAmount(amount);
    this.amount = amount;
  }

  /**
   * Returns the unit of the ingredient item.
   *
   * @return the unit of the ingredient item
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Sets the unit of the ingredient item.
   *
   * @param unit the new unit of the ingredient item
   * @throws IllegalArgumentException if unit is null or empty
   */
  public void setUnit(String unit) {
    validateString(unit, "Unit");
    this.unit = unit;
  }

  /**
   * Returns the price of the ingredient item.
   *
   * @return the price of the ingredient item
   */
  public double getPrice() {
    return price;
  }

  /**
   * Sets the price of the ingredient item.
   *
   * @param price the new price of the ingredient item
   * @throws IllegalArgumentException if price is negative
   */
  public void setPrice(double price) {
    validatePrice(price);
    this.price = price;
  }

  /**
   * Returns the expiration date of the ingredient item.
   *
   * @return the expiration date of the ingredient item
   */
  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  /**
   * Sets the expiration date of the ingredient item.
   *
   * @param expirationDate the new expiration date of the ingredient item
   * @throws IllegalArgumentException if expirationDate is null or in the past
   */
  public void setExpirationDate(LocalDate expirationDate) {
    validateExpirationDate(expirationDate);
    this.expirationDate = expirationDate;
  }

  /**
   * Checks if the ingredient item is expired.
   *
   * @return {@code true} if the ingredient item is expired, {@code false} otherwise
   */
  public boolean isExpired() {
    return LocalDate.now().isAfter(expirationDate);
  }

  /**
   * Returns a string representation of the ingredient item.
   *
   * @return a string representation of the ingredient item
   */
  @Override
  public String toString() {
    return String.format(
        "Ingredient{name='%s', amount=%.2f %s, price=%.2f, expirationDate=%s}",
        name, amount, unit, price, expirationDate);
  }

  /**
   * Validates that a string is not null or empty.
   *
   * @param value     the string to validate
   * @param fieldName the name of the field for error messages
   * @throws IllegalArgumentException if value is null or empty
   */
  private void validateString(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " cannot be null or blank");
    }
  }

  /**
   * Validates that the amount is not negative.
   *
   * @param amount the amount to validate
   * @throws IllegalArgumentException if amount is negative
   */
  private void validateAmount(double amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
  }

  /**
   * Validates that the price is not negative.
   *
   * @param price the price to validate
   * @throws IllegalArgumentException if price is negative
   */
  private void validatePrice(double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
  }

  /**
   * Validates that the expiration date is not null or in the past.
   *
   * @param expirationDate the expiration date to validate
   * @throws IllegalArgumentException if expirationDate is null or in the past
   */
  private void validateExpirationDate(LocalDate expirationDate) {
    if (expirationDate == null) {
      throw new IllegalArgumentException("Expiration date cannot be null");
    }
    if (expirationDate.isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("Expiration date cannot be in the past");
    }
  }

  /**
   * Checks if this ingredient item is equal to another object.
   *
   * @param o the object to compare with
   * @return {@code true} if this ingredient item is equal to the other object, {@code false}
   * otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Ingredient ingredient)) {
      return false;
    }
    return name.equals(ingredient.name)
        && unit.equals(ingredient.unit)
        && expirationDate.equals(ingredient.expirationDate);
  }
}