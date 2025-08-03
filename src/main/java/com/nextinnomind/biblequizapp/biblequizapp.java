package com.nextinnomind.biblequizapp;

import com.nextinnomind.biblequizapp.Manager.ShutdownManager;
import com.nextinnomind.biblequizapp.Manager.SplashScreenManager;
import com.nextinnomind.biblequizapp.Loader.ViewLoader;
import com.nextinnomind.biblequizapp.Manager.ViewModeSelectorManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class biblequizapp extends Application {

    private static final Logger logger = LogManager.getLogger(biblequizapp.class);
    private ViewModeSelectorManager selector;

    @Override
    public void start(Stage primaryStage) {
        logger.info("Application startup initiated...");

        // Initialize splash screen and startup sequence
        SplashScreenManager.initialize();

        // Setup view mode selection and load main view
        selector = new ViewModeSelectorManager();
        String viewMode = selector.getViewMode();
        ViewLoader.loadMainView(primaryStage, viewMode,"");

        // Setup shutdown hook
        ShutdownManager.setupShutdownHook(selector);
    }

    @Override
    public void stop() {
        logger.info("Application is stopping...");
        ShutdownManager.cleanup(selector);
    }

    public static void main(String[] args) {
        logger.info("Launching JavaFX application...");
        launch(args);
    }
}