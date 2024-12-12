package edu.ntnu.idi.idatt.service;

import edu.ntnu.idi.idatt.manager.Cookbook;
import edu.ntnu.idi.idatt.manager.FoodStorage;
import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import edu.ntnu.idi.idatt.util.UserInputHandler;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Service class for the CookbookManager.
 *
 * <p>This class provides methods for things such as adding, removing, and querying recipes</p>
 *
 * <p>This class is responsible for handling user input and interacting with the CookbookManager
 * the storage.
 *
 * @author Kristian Ask Selmer
 */
public class CookbookService {

  private final Cookbook cookbook;
  private final FoodStorage foodStorage;
  private final Scanner scanner;

  /**
   * Constructs a new CookbookService with the specified CookbookManager and Scanner.
   *
   * @param cookbook    the CookbookManager to interact with
   * @param scanner     the Scanner for user input
   * @param foodStorage the FoodStorageManager to interact with
   */
  public CookbookService(Cookbook cookbook, FoodStorage foodStorage, Scanner scanner) {
    this.foodStorage = foodStorage;
    this.cookbook = cookbook;
    this.scanner = scanner;
  }

  /**
   * Takes user input to create an Ingredient object for a recipe. The expiration date is
   * automatically set to one year from today.
   *
   * @return the created Ingredient object
   */
  private Ingredient createRecipeIngredient() {
    String name = UserInputHandler.takeStringInput(scanner, "Ingredient name");
    String unit = UserInputHandler.takeStringInput(scanner, "Ingredient unit");
    double amount = UserInputHandler.takeDoubleInput(scanner, "Ingredient amount");
    LocalDate expirationDate = LocalDate.now().plusYears(1);

    return new Ingredient(name, amount, unit, 1, expirationDate);
  }

  /**
   * Add a new recipe to the cookbook based on user input.
   */
  public void addRecipe() {
    String name = UserInputHandler.takeStringInput(scanner, "Recipe name");
    String description = UserInputHandler.takeStringInput(scanner, "Recipe description");
    String instructions = UserInputHandler.takeStringInput(scanner, "Recipe instructions");
    int servings = UserInputHandler.takeIntInput(scanner, "Number of servings");
    Recipe recipe;

    try {
      recipe = new Recipe(name, description, instructions, servings);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }
    System.out.println(
        "You can now add ingredients to the recipe. Type 'yes' to add an ingredient or 'done' to finish.");

    while (true) {
      String input = UserInputHandler.takeStringInput(scanner, "Add ingredient? (yes/done)");
      if (input.equalsIgnoreCase("done")) {
        break;
      } else if (input.equalsIgnoreCase("yes")) {
        try {
          Ingredient ingredient = createRecipeIngredient();
          recipe.addIngredient(ingredient);
          System.out.println("Ingredient added to the recipe.");
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
        }
      } else {
        System.out.println(
            "Invalid input. Please type 'yes' to add an ingredient or 'done' to finish.");
      }
    }

    if (recipe.getIngredients().isEmpty()) {
      System.out.println("Recipe must have at least one ingredient.");
      return;
    }

    try {
      cookbook.addRecipe(recipe);
      System.out.println("Recipe added successfully!");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Prints all the recipes in a nicely formatted way.
   *
   * @param recipes the list of recipes to print
   */
  private void printRecipes(List<Recipe> recipes) {
    if (recipes == null || recipes.isEmpty()) {
      System.out.println("No recipes to display.");
      return;
    }

    System.out.println("Recipe List");
    for (int i = 0; i < recipes.size(); i++) {
      Recipe recipe = recipes.get(i);
      System.out.printf("\nRecipe #%d:\n", i + 1);
      System.out.println("Name: " + recipe.getName());
      System.out.println("Description: " + recipe.getDescription());
      System.out.println("Instructions: " + recipe.getInstructions());
      System.out.println("Servings: " + recipe.getServings());
      System.out.println("Ingredients:");
      if (recipe.getIngredients().isEmpty()) {
        System.out.println("  - No ingredients listed.");
      } else {
        for (Ingredient ingredient : recipe.getIngredients()) {
          System.out.println("  - " + ingredient.prettyPrint());
        }
      }
    }
    System.out.println();
  }

  /**
   * Prints all the recipes in the cookbook.
   */
  public void printAllRecipes() {
    List<Recipe> recipes = cookbook.getAllRecipes();
    printRecipes(recipes);
  }

  /**
   * Checks if a given recipe can be prepared from the ingredients in the provided FoodStorage.
   */
  public void canPrepareRecipe() {
    String name = UserInputHandler.takeStringInput(scanner, "Recipe name");
    try {
      Recipe recipe = cookbook.getRecipe(name);
      if (cookbook.canPrepareRecipe(recipe, foodStorage)) {
        System.out.println("You can prepare the recipe!");
      } else {
        System.out.println("You cannot prepare the recipe.");
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Suggestions for recipes that can be made with the ingredients in the food storage.
   */
  public void suggestRecipes() {
    List<Recipe> recipes = cookbook.suggestRecipes(foodStorage);

    if (recipes.isEmpty()) {
      System.out.println("No recipes can be made with the ingredients in the food storage.");
      return;
    }

    System.out.println("Suggested recipes:");
    for (Recipe recipe : recipes) {
      System.out.println(recipe.getName());
    }
  }
}
