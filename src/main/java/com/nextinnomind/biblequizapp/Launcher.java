package com.nextinnomind.biblequizapp;

/**
 * Launcher class to avoid module system issues with JavaFX
 * This class should NOT extend Application
 */
public class Launcher {
    public static void main(String[] args) {
        // Set JavaFX system properties to avoid module issues
        System.setProperty("javafx.preloader", "");
        System.setProperty("java.awt.headless", "false");

        // Launch the actual JavaFX application
        HelloApplication.main(args);
    }
}