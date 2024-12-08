package edu.ntnu.idi.idatt.service;

import edu.ntnu.idi.idatt.manager.FoodStorage;
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


}
