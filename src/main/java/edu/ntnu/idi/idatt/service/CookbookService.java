package edu.ntnu.idi.idatt.service;

import edu.ntnu.idi.idatt.manager.Cookbook;
import edu.ntnu.idi.idatt.manager.FoodStorage;
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

}
