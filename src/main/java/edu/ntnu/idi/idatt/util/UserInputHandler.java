package edu.ntnu.idi.idatt.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Handles user input validation and parsing.
 *
 * <p>This class provides methods to validate and parse user input for integers, doubles, and
 * dates.</p>
 *
 * @author Kristian Ask Selmer
 */
public class UserInputHandler {

  /**
   * Validates and returns an integer input.
   *
   * @param scanner The Scanner object for user input.
   * @param prompt  The message to display to the user.
   * @return A valid integer.
   */
  public static int takeIntInput(Scanner scanner, String prompt) {
    while (true) {
      System.out.print(prompt + ": ");
      String input = scanner.nextLine();
      try {
        return Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid integer.");
      }
    }
  }

  /**
   * Validates and returns a double input.
   *
   * @param scanner The Scanner object for user input.
   * @param prompt  The message to display to the user.
   * @return A valid double.
   */
  public static double takeDoubleInput(Scanner scanner, String prompt) {
    while (true) {
      System.out.print(prompt + ": ");
      String input = scanner.nextLine();
      try {
        return Double.parseDouble(input);
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid double.");
      }
    }
  }

  /**
   * Validates and returns a date in the format dd-MM-yyyy.
   *
   * @param scanner The Scanner object for user input.
   * @param prompt  The message to display to the user.
   * @return A valid LocalDate object.
   */
  public static LocalDate takeDateInput(Scanner scanner, String prompt) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    while (true) {
      System.out.print(prompt + " (dd-MM-yyyy): ");
      String input = scanner.nextLine();
      try {
        return LocalDate.parse(input, formatter);
      } catch (DateTimeParseException e) {
        System.out.println("Invalid date format. Please use dd-MM-yyyy.");
      }
    }
  }

  /**
   * Validates and returns a string input.
   *
   * @param scanner The Scanner object for user input.
   * @param prompt  The message to display to the user.
   * @return A valid string.
   */
  public static String takeStringInput(Scanner scanner, String prompt) {
    while (true) {
      System.out.print(prompt + ": ");
      String input = scanner.nextLine();
      if (input.isBlank()) {
        System.out.println("Input cannot be blank.");
        continue;
      }
      try {
        Double.parseDouble(input);
        System.out.println("Input cannot be a number.");
        continue;
      } catch (NumberFormatException e) {
      }
      return input;

    }
  }
}