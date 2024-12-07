package edu.ntnu.idi.idatt.manager;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages a collection of recipes in a cookbook.
 *
 * <p>This class allows adding, removing, and searching for recipes by name. It also provides
 * functionality to get suggested recipes based on the current availability of ingredients in a
 * provided {@link FoodStorage} instance.</p>
 *
 * @author Kristian Ask Selmer
 */
public class Cookbook {

  private final Map<String, Recipe> recipes;

  /**
   * Constructs a new Cookbook with no recipes.
   */
  public Cookbook() {
    this.recipes = new HashMap<>();
  }

  /**
   * Adds a new recipe to the cookbook.
   *
   * <p>If a recipe with the same name already exists (case-insensitive),
   * it will be replaced by the new one.</p>
   *
   * @param recipe the recipe to add
   * @throws IllegalArgumentException if the recipe is null or has a null/empty name
   */
  public void addRecipe(Recipe recipe) {
    if (recipe == null) {
      throw new IllegalArgumentException("Recipe must not be null");
    }
    String key = validateAndGetKey(recipe.getName());
    recipes.put(key, recipe);
  }

  /**
   * Removes a recipe from the cookbook by its name.
   *
   * @param name the name of the recipe to remove
   * @return the removed recipe if found, or null if no recipe with that name exists
   * @throws IllegalArgumentException if the name is null or empty
   */
  public Recipe removeRecipe(String name) {
    String key = validateAndGetKey(name);
    return recipes.remove(key);
  }

  /**
   * Searches for a recipe in the cookbook by its name.
   *
   * <p>The search is case-insensitive.</p>
   *
   * @param name the name of the recipe to search for
   * @return the recipe if found, or null if no recipe with that name exists
   * @throws IllegalArgumentException if the name is null or empty
   */
  public Recipe getRecipe(String name) {
    String key = validateAndGetKey(name);
    return recipes.get(key);
  }

  /**
   * Searches for all recipes containing a given keyword in their name.
   *
   * <p>The search is case-insensitive.</p>
   *
   * @param keyword the keyword to search for
   * @return a list of recipes whose names contain the keyword, or an empty list if none match
   */
  public List<Recipe> searchRecipes(String keyword) {
    if (keyword == null || keyword.isBlank()) {
      return Collections.emptyList();
    }
    String lowerKeyword = keyword.toLowerCase();
    return recipes.entrySet().stream()
        .filter(entry -> entry.getKey().contains(lowerKeyword))
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());
  }

  /**
   * Returns a list of recipe names stored in the cookbook.
   *
   * @return a list of all recipe names in the cookbook
   */
  public List<String> getAllRecipeNames() {
    return new ArrayList<>(recipes.keySet()).stream()
        .map(key -> recipes.get(key).getName())
        .collect(Collectors.toList());
  }

  /**
   * Suggests recipes that can be prepared from the ingredients available in the provided
   * FoodStorage.
   *
   * <p>This method returns all recipes for which the FoodStorage has sufficient non-expired
   * ingredients. If no recipes are found, an empty list is returned.</p>
   *
   * @param foodStorage the FoodStorage to check against
   * @return a list of recipes that can be prepared with the given foodStorage, or an empty list if
   * none match
   * @throws IllegalArgumentException if foodStorage is null
   */
  public List<Recipe> suggestRecipes(FoodStorage foodStorage) {
    if (foodStorage == null) {
      throw new IllegalArgumentException("FoodStorage must not be null");
    }

    return recipes.values().stream()
        .filter(recipe -> canPrepareRecipe(recipe, foodStorage))
        .collect(Collectors.toList());
  }

  /**
   * Checks if a given recipe can be prepared from the ingredients in the provided FoodStorage.
   *
   * @param recipe      the recipe to check
   * @param foodStorage the FoodStorage to check against
   * @return true if the recipe can be prepared, false otherwise
   */
  private boolean canPrepareRecipe(Recipe recipe, FoodStorage foodStorage) {
    if (recipe == null) {
      return false;
    }

    for (Ingredient required : recipe.getIngredients()) {
      String key = required.getName().toLowerCase();
      double totalAvailable = foodStorage.getAllIngredients().stream()
          .filter(i -> i.getName().equalsIgnoreCase(required.getName()))
          .filter(i -> !i.isExpired())
          .filter(i -> i.getUnit().equals(required.getUnit()))
          .mapToDouble(Ingredient::getAmount)
          .sum();

      if (totalAvailable < required.getAmount()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Validates that a recipe or search key is not null or empty, and returns a lowercase key.
   *
   * @param name the recipe name or search key to validate
   * @return the lowercase key
   * @throws IllegalArgumentException if name is null or empty
   */
  private String validateAndGetKey(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or blank");
    }
    return name.toLowerCase();
  }
}