package edu.ntnu.idi.idatt.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a culinary recipe with details such as name, description, instructions, number of
 * servings, and a list of ingredients.
 *
 * <p>This class allows you to create a recipe that includes detailed information and
 * manage its ingredients using the {@code Ingredient} class.</p>
 *
 * @author Kristian Ask Selmer
 */
public class Recipe {

  private final List<Ingredient> ingredients;
  private String name;
  private String description;
  private String instructions;
  private int servings;

  /**
   * Constructs a new {@code Recipe} with the specified details.
   *
   * @param name         the name of the recipe
   * @param description  a brief overview of the recipe
   * @param instructions the steps to prepare the recipe
   * @param servings     the number of servings the recipe provides
   * @throws IllegalArgumentException if any argument is invalid
   */
  public Recipe(String name, String description, String instructions, int servings) {
    validateString(name, "Name must not be null or empty");
    validateString(description, "Description must not be null or empty");
    validateString(instructions, "Instructions must not be null or empty");
    validatePositive(servings, "Servings must be greater than zero");

    this.name = name;
    this.description = description;
    this.instructions = instructions;
    this.servings = servings;
    this.ingredients = new ArrayList<>();
  }

  /**
   * Retrieves the name of the recipe.
   *
   * @return the recipe name
   */
  public String getName() {
    return name;
  }

  /**
   * Updates the name of the recipe.
   *
   * @param name the new name
   * @throws IllegalArgumentException if the name is invalid
   */
  public void setName(String name) {
    validateString(name, "Name must not be null or empty");
    this.name = name;
  }

  /**
   * Retrieves the description of the recipe.
   *
   * @return the recipe description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Updates the description of the recipe.
   *
   * @param description the new description
   * @throws IllegalArgumentException if the description is invalid
   */
  public void setDescription(String description) {
    validateString(description, "Description must not be null or empty");
    this.description = description;
  }

  /**
   * Retrieves the instructions for preparing the recipe.
   *
   * @return the cooking instructions
   */
  public String getInstructions() {
    return instructions;
  }

  /**
   * Updates the cooking instructions of the recipe.
   *
   * @param instructions the new instructions
   * @throws IllegalArgumentException if the instructions are invalid
   */
  public void setInstructions(String instructions) {
    validateString(instructions, "Instructions must not be null or empty");
    this.instructions = instructions;
  }

  /**
   * Retrieves the number of servings the recipe yields.
   *
   * @return the number of servings
   */
  public int getServings() {
    return servings;
  }

  /**
   * Updates the number of servings.
   *
   * @param servings the new number of servings
   * @throws IllegalArgumentException if servings is not positive
   */
  public void setServings(int servings) {
    validatePositive(servings, "Servings must be greater than zero");
    this.servings = servings;
  }

  /**
   * Retrieves the list of ingredients required for the recipe.
   *
   * @return a list of ingredients
   */
  public List<Ingredient> getIngredients() {
    return new ArrayList<>(ingredients);
  }

  /**
   * Adds an ingredient to the recipe.
   *
   * @param ingredient the ingredient to add
   * @throws IllegalArgumentException if the ingredient is null
   */
  public void addIngredient(Ingredient ingredient) {
    if (ingredient == null) {
      throw new IllegalArgumentException("Ingredient must not be null");
    }
    ingredients.add(ingredient);
  }

  /**
   * Removes an ingredient from the recipe.
   *
   * @param ingredient the ingredient to remove
   * @throws IllegalArgumentException if the ingredient is null or not found
   */
  public void removeIngredient(Ingredient ingredient) {
    if (ingredient == null) {
      throw new IllegalArgumentException("Ingredient must not be null");
    }
    if (!ingredients.remove(ingredient)) {
      throw new IllegalArgumentException("Ingredient not found in the recipe");
    }
  }

  /**
   * Validates that a string is not null or empty.
   *
   * @param value        the string to validate
   * @param errorMessage the error message to display if validation fails
   * @throws IllegalArgumentException if the string is invalid
   */
  private void validateString(String value, String errorMessage) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  /**
   * Validates that an integer is positive.
   *
   * @param value        the integer to validate
   * @param errorMessage the error message to display if validation fails
   * @throws IllegalArgumentException if the integer is not positive
   */
  private void validatePositive(int value, String errorMessage) {
    if (value <= 0) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  /**
   * Provides a string representation of the recipe, including its details and ingredients.
   *
   * @return a string describing the recipe
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Recipe Name: ").append(name).append("\n")
        .append("Description: ").append(description).append("\n")
        .append("Instructions: ").append(instructions).append("\n")
        .append("Servings: ").append(servings).append("\n")
        .append("Ingredients:\n");
    for (Ingredient ingredient : ingredients) {
      sb.append("- ").append(ingredient).append("\n");
    }
    return sb.toString();
  }
}