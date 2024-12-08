package edu.ntnu.idi.idatt.service;

import edu.ntnu.idi.idatt.manager.FoodStorage;
import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.util.UserInputHandler;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Service class for the FoodStorageManager.
 *
 * <p>This class provides methods for things such ass adding, removing, and querying ingredients in
 * It also allows you to find ingredients by various criteria, calculate total values, and check
 * ingredient availability for recipes.</p>
 *
 * <p>This class is responsible for handling user input and interacting with the FoodStorageManager
 * the storage.
 *
 * @author Kristian Ask Selmer
 */
public class FoodStorageService {

  private final FoodStorage foodStorage;
  private final Scanner scanner;

  /**
   * Constructs a new FoodStorageService with the specified FoodStorageManager and Scanner.
   *
   * @param foodStorage the FoodStorageManager to interact with
   * @param scanner     the Scanner for user input
   */
  public FoodStorageService(FoodStorage foodStorage, Scanner scanner) {
    this.foodStorage = foodStorage;
    this.scanner = scanner;
  }

  /**
   * Add an ingredient to the storage from user input.
   */
  public void addIngredient() {
    String name = UserInputHandler.takeStringInput(scanner, "Ingredient name");
    String unit = UserInputHandler.takeStringInput(scanner, "Ingredient unit");
    double amount = UserInputHandler.takeDoubleInput(scanner, "Ingredient amount");
    double price = UserInputHandler.takeDoubleInput(scanner, "Ingredient price");
    LocalDate expirationDate = UserInputHandler.takeDateInput(scanner,
        "Ingredient expiration date");
    try {
      foodStorage.addIngredient(
          new Ingredient(name, amount, unit, price, expirationDate));
      System.out.println("Ingredient added successfully.");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Remove an ingredient from the food storage.
   */
  public void removeIngredient() {
    String name = UserInputHandler.takeStringInput(scanner, "Ingredient name");
    double amount = UserInputHandler.takeDoubleInput(scanner, "Ingredient amount");
    if (amount < 0) {
      System.out.println("Invalid amount. Please try again.");
      return;
    }
    try {
      foodStorage.consumeIngredient(name, amount);
      System.out.println("Ingredient removed successfully.");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

  }

  /**
   * Print all the ingredients in the food storage
   */
  public void printIngredients() {
    System.out.println(foodStorage.stringRepresentation());
  }


}
