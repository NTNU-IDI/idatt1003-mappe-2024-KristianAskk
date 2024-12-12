package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.manager.Cookbook;
import edu.ntnu.idi.idatt.manager.FoodStorage;
import edu.ntnu.idi.idatt.service.CookbookService;
import edu.ntnu.idi.idatt.service.FoodStorageService;
import edu.ntnu.idi.idatt.util.UserInputHandler;
import java.util.Scanner;

/**
 * User interface for the application.
 *
 * <p>This class provides methods for user input and interaction with the application.</p>
 *
 * @author Kristian Ask Selmer
 */
public class UserInterface {

  private FoodStorageService foodStorageService;
  private CookbookService cookbookService;
  private Scanner scanner;

  /**
   * Prints the menu to the user.
   */
  private static void printMenu() {
    System.out.println("1. Add an ingredient");
    System.out.println("2. Remove an ingredient");
    System.out.println("3. Print all ingredients");
    System.out.println("4. Print total value of ingredients");
    System.out.println("5. Search for ingredients");
    System.out.println("6. Print expired ingredients");
    System.out.println("7. Add Recipe to Cookbook");
    System.out.println("8. Print all recipes");
    System.out.println("9. Check if you can prepare a recipe");
    System.out.println("10. Suggest recipes that can be made with ingredients in the food storage");
    System.out.println("11. Exit");
  }

  /**
   * Initializes the application.
   */
  public void init() {
    FoodStorage foodStorage = new FoodStorage();
    Cookbook cookbook = new Cookbook();
    Scanner scanner = new Scanner(System.in);

    foodStorageService = new FoodStorageService(foodStorage, scanner);
    cookbookService = new CookbookService(cookbook, foodStorage, scanner);
    this.scanner = scanner;
  }

  /**
   * Starts the application.
   */
  public void start() {
    System.out.println(" ___             _   ___  ");
    System.out.println("| __|__  ___  __| | / __| __ ___ _____ _ _ ");
    System.out.println("| _/ _ \\/ _ \\/ _` | \\__ \\/ _` \\ V / -_) '_|");
    System.out.println("|_|\\___/\\___/\\__,_| |___/\\__,_|\\_/\\___|_|  ");
    System.out.println();
    System.out.println("Welcome to the food saver app!");
    System.out.println();

    boolean abort = false;
    while (!abort) {
      System.out.println();
      printMenu();
      System.out.println();

      int action = UserInputHandler.takeIntInput(scanner, "Choose which action to perform");
      switch (action) {
        case 1 -> foodStorageService.addIngredient();
        case 2 -> foodStorageService.removeIngredient();
        case 3 -> foodStorageService.printIngredients();
        case 4 -> foodStorageService.printTotalValueOfIngredients();
        case 5 -> foodStorageService.searchForIngredients();
        case 6 -> foodStorageService.printExpiredIngredients();
        case 7 -> cookbookService.addRecipe();
        case 8 -> cookbookService.printAllRecipes();
        case 9 -> cookbookService.canPrepareRecipe();
        case 10 -> cookbookService.suggestRecipes();
        case 11 -> abort = true;
        default -> System.out.println("Invalid action. Please try again.");
      }
    }
    System.out.println("Goodbye!");
  }
}