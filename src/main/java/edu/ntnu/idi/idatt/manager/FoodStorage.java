package edu.ntnu.idi.idatt.manager;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages a collection of ingredients in the food storage.
 *
 * <p>This class provides methods to add, remove, and query ingredients in the storage.
 * It also allows you to find ingredients by various criteria, calculate total values, and check
 * ingredient availability for recipes.</p>
 *
 * @author Kristian Ask Selmer
 */
public class FoodStorage {

  private final Map<String, List<Ingredient>> ingredients;

  /**
   * Constructs a new FoodStorage with an empty collection of ingredients.
   */
  public FoodStorage() {
    this.ingredients = new HashMap<>();
  }

  /**
   * Adds an ingredient to the storage.
   *
   * <p>If an ingredient with the same name, unit, price, and expiration date exists,
   * it increments its amount. Otherwise, it adds a new ingredient to the list.</p>
   *
   * @param ingredient the ingredient to add
   * @throws IllegalArgumentException if the ingredient is null
   */
  public void addIngredient(Ingredient ingredient) {
    if (ingredient == null) {
      throw new IllegalArgumentException("Ingredient must not be null");
    }

    String key = ingredient.getName().toLowerCase();
    ingredients.putIfAbsent(key, new ArrayList<>());
    List<Ingredient> ingredientList = ingredients.get(key);
    boolean isMerged = false;
    ListIterator<Ingredient> iterator = ingredientList.listIterator();

    while (iterator.hasNext()) {
      Ingredient existing = iterator.next();
      if (existing.getUnit().equals(ingredient.getUnit())
          && existing.getExpirationDate().equals(ingredient.getExpirationDate())) {
        double newAmount = existing.getAmount() + ingredient.getAmount();
        double newPrice = existing.getPrice() + ingredient.getPrice();
        existing.setAmount(newAmount);
        existing.setPrice(newPrice);
        isMerged = true;
      }
    }

    if (!isMerged) {
      ingredientList.add(ingredient);
    }
  }

  /**
   * Removes a specified amount of an ingredient from the storage, excluding expired ingredients.
   *
   * <p>Ingredients are consumed based on their expiration dates, starting with the earliest.
   * If the requested amount exceeds the total available from non-expired ingredients, an exception
   * is thrown.</p>
   *
   * @param name   the name of the ingredient
   * @param amount the amount to remove
   * @throws IllegalArgumentException if the ingredient doesn't exist or not enough quantity
   */
  public void consumeIngredient(String name, double amount) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Ingredient name must not be null or blank");
    }
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount to consume must be positive");
    }

    String key = name.toLowerCase();
    List<Ingredient> ingredientList = ingredients.get(key);

    if (ingredientList == null || ingredientList.isEmpty()) {
      throw new IllegalArgumentException("Ingredient not found in storage");
    }

    List<Ingredient> availableIngredients = new ArrayList<>();
    for (Ingredient ing : ingredientList) {
      if (!ing.isExpired()) {
        availableIngredients.add(ing);
      }
    }

    if (availableIngredients.isEmpty()) {
      throw new IllegalArgumentException("No ingredients available for: " + name);
    }

    Collections.sort(availableIngredients, Comparator.comparing(Ingredient::getExpirationDate));

    double totalAvailable = 0;
    for (Ingredient ing : availableIngredients) {
      totalAvailable += ing.getAmount();
    }

    if (amount > totalAvailable) {
      String unit = availableIngredients.getFirst().getUnit();
      throw new IllegalArgumentException(
          "Insufficient quantity available for " + name + ". Available: "
              + totalAvailable + unit);
    }

    double remainingAmount = amount;
    for (Ingredient ing : availableIngredients) {
      if (remainingAmount <= 0) {
        break;
      }

      double currentAmount = ing.getAmount();
      if (currentAmount <= remainingAmount) {
        remainingAmount -= currentAmount;
        ingredientList.remove(ing);
      } else {
        double newPrice = ing.getPrice() * (ing.getAmount() - remainingAmount) / ing.getAmount();
        ing.setPrice(newPrice);
        ing.setAmount(currentAmount - remainingAmount);
        remainingAmount = 0;
      }
    }

    ingredientList.removeIf(Ingredient::isExpired);

    if (ingredientList.isEmpty()) {
      ingredients.remove(key);
    }
  }

  /**
   * Checks if the storage has enough of each ingredient required for a recipe, excluding expired
   * ingredients.
   *
   * @param recipe the recipe to check against
   * @return {@code true} if all ingredients are available in sufficient quantities, {@code false}
   * otherwise
   */
  public boolean canPrepareRecipe(Recipe recipe) {
    if (recipe == null) {
      throw new IllegalArgumentException("Recipe must not be null");
    }

    for (Ingredient required : recipe.getIngredients()) {
      String key = required.getName().toLowerCase();
      List<Ingredient> availableIngredients = ingredients.getOrDefault(key, Collections.emptyList())
          .stream()
          .filter(ingredient -> !ingredient.isExpired())
          .filter(ingredient -> ingredient.getUnit().equals(required.getUnit()))
          .collect(Collectors.toList());

      double totalAvailable = availableIngredients.stream()
          .mapToDouble(Ingredient::getAmount)
          .sum();

      if (totalAvailable < required.getAmount()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Removes the ingredients required for a recipe from the storage, excluding expired ingredients.
   *
   * @param recipe the recipe to prepare
   * @throws IllegalArgumentException if ingredients are insufficient
   */
  public void prepareRecipe(Recipe recipe) {
    if (!canPrepareRecipe(recipe)) {
      throw new IllegalArgumentException(
          "Insufficient non-expired ingredients to prepare the recipe");
    }

    for (Ingredient required : recipe.getIngredients()) {
      consumeIngredient(required.getName(), required.getAmount());
    }
  }

  /**
   * Retrieves a list of all ingredients in the storage.
   *
   * @return a list of all ingredients
   */
  public List<Ingredient> getAllIngredients() {
    return ingredients.values().stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves ingredients that have expired.
   *
   * @return a list of expired ingredients
   */
  public List<Ingredient> getExpiredIngredients() {
    LocalDate today = LocalDate.now();
    return ingredients.values().stream()
        .flatMap(Collection::stream)
        .filter(ingredient -> ingredient.getExpirationDate().isBefore(today))
        .collect(Collectors.toList());
  }

  /**
   * Searches for ingredients containing a specific keyword in their name.
   *
   * @param keyword the keyword to search for
   * @return a list of matching ingredients
   */
  public List<Ingredient> searchIngredientsByName(String keyword) {
    if (keyword == null || keyword.isBlank()) {
      return Collections.emptyList();
    }

    String lowerKeyword = keyword.toLowerCase();
    return ingredients.entrySet().stream()
        .filter(entry -> entry.getKey().contains(lowerKeyword))
        .flatMap(entry -> entry.getValue().stream())
        .collect(Collectors.toList());
  }

  /**
   * Returns the number of unique ingredient types in the storage.
   *
   * @return the number of ingredient types
   */
  public int getIngredientTypeCount() {
    return ingredients.size();
  }

  /**
   * Calculates the total value of ingredients in the storage.
   *
   * @return the total value
   */
  public double calculateTotalValue() {
    return ingredients.values().stream()
        .flatMap(Collection::stream)
        .mapToDouble(ingredient -> ingredient.getPrice())
        .sum();
  }

  /**
   * Returns a string representation of the food storage.
   *
   * @return a string detailing the contents of the storage
   */
  public String stringRepresentation() {
    StringBuilder sb = new StringBuilder("Food Storage Contents:\n");

    if (ingredients.isEmpty()) {
      sb.append("No ingredients in storage.");
      return sb.toString();
    }

    for (Map.Entry<String, List<Ingredient>> entry : ingredients.entrySet()) {
      sb.append("Ingredient: ").append(entry.getKey()).append("\n");
      for (Ingredient ingredient : entry.getValue()) {
        sb.append("  - ").append(ingredient.prettyPrint()).append("\n");
      }
    }
    return sb.toString();
  }
}