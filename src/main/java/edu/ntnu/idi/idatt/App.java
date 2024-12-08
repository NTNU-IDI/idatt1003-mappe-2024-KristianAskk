package edu.ntnu.idi.idatt;

/**
 * Entry point for the application.
 *
 * <p>This class contains the main method for the application, which initializes the UserInterface
 * and starts the application.</p>
 *
 * @author Kristian Ask Selmer
 */
public class App {

  /**
   * The main method for the application.
   */
  public static void main(String[] args) {
    UserInterface ui = new UserInterface();
    ui.init();
    ui.start();
  }

}
