[![Review Assignment Due Date]([x]https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/INcAwgxk)
# Portfolio project IDATT1003
This file uses Mark Down syntax. For more information see [here]([x]https://www.markdownguide.org/basic-syntax/).

[//]: # (TODO: Fill inn your name and student ID)

STUDENT NAME = "Kristian Ask Selmer"
STUDENT ID = "10094"

## Project description

This project is a portfolio application for managing and storing recipes. It includes functionality for adding ingredients, creating recipes, managing a cookbook, and tracking food storage.

## Project structure

- **Source Files**:
  - Located in `src/main/java/edu/ntnu/idi/idatt/`
  - Includes:
    - `App.java` (Main class)
    - `UserInterface.java`
    - `manager/` (Cookbook and FoodStorage logic)
    - `model/` (Ingredient and Recipe classes)
    - `service/` (Service classes for managing cookbook and storage logic)
    - `util/` (UserInputHandler for handling input)
  
- **Test Files**:
  - Located in `src/test/java/edu/ntnu/idi/idatt/`
  - Includes:
    - Unit tests for `Cookbook`, `FoodStorage`, `Ingredient`, and `Recipe`.

## Link to repository

[//]: # (TODO: Include a link to your GitHub repository here.)

## How to run the project

1. `mvn clean install`
2. `java -cp target/idatt1003-mappe-2024-KristianAskk-1.0.0.jar edu.ntnu.idi.idatt.App`

## How to run the tests

Run `mvn test`

