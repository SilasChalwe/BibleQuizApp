package com.nextinnomind.biblequizapp;

/**
 * Launcher class to avoid module system issues with JavaFX
 * This class should NOT extend Application
 */
public class Launcher {
    public static void main(String[] args) {
        // Launch the actual JavaFX application
        biblequizapp.main(args);
    }
}