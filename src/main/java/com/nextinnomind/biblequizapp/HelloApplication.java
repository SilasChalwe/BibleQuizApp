package com.nextinnomind.biblequizapp;

import com.nextinnomind.biblequizapp.utils.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class HelloApplication extends Application {

    private static final Logger logger = LogManager.getLogger(HelloApplication.class);

    private ViewModeSelector selector;
    private static Stage splashStage;

    private static final double SPLASH_WIDTH = 600;
    private static final double SPLASH_HEIGHT = 400;

    @Override
    public void start(Stage primaryStage) {
        logger.info("Application startup initiated...");

        Platform.runLater(() -> {
            try {
                Thread.sleep(1000);
                showSplashScreen();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            JsonDataLoader.getInstance();
            showViewModeDialogAndLoadMain(primaryStage);

            new Thread(() -> {
                try {
                    logger.info("Removing duplicate questions...");
                    JsonUtils.removeDuplicateQuestions();

                    logger.info("Loading data...");
                    JsonDataLoader.getInstance();

                    Thread.sleep(10_000);

                    Platform.runLater(() -> {
                        stopAllAudio();
                        closeSplashScreen();
                    });

                } catch (Exception e) {
                    logger.error("Error during startup: {}", e.getMessage(), e);
                    Platform.exit();
                }
            }).start();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cleanup();
            logger.info("JVM shutdown hook triggered.");

            if (selector != null) selector.clearSavedChoice();
            stopAllAudio();
            //cleanup();
        }));
    }

    private static void showSplashScreen() {
        splashStage = new Stage(StageStyle.UNDECORATED);

        StackPane splashRoot = new StackPane();
        splashRoot.setStyle("-fx-background-color: #2a2a2a;");

        Image splashImage = new Image(Objects.requireNonNull(
                HelloApplication.class.getResourceAsStream("/com/nextinnomind/biblequizapp/assets/splash.png")));
        ImageView splashImageView = new ImageView(splashImage);
        splashImageView.setPreserveRatio(true);
        splashImageView.setFitWidth(SPLASH_WIDTH);

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);

        splashRoot.getChildren().addAll(splashImageView, progressIndicator);
        StackPane.setAlignment(progressIndicator, Pos.BOTTOM_CENTER);
        StackPane.setMargin(progressIndicator, new Insets(0, 0, 20, 0));

        Scene splashScene = new Scene(splashRoot, SPLASH_WIDTH, SPLASH_HEIGHT);
        splashStage.setScene(splashScene);
        splashStage.getIcons().add(splashImage);
        splashStage.setAlwaysOnTop(true);
        splashStage.show();

        // Start splash audio
        SoundPlayer.play("/com/nextinnomind/biblequizapp/assets/splash.wav");
    }

    private static void stopAllAudio() {
        SoundPlayer.stop();
        logger.debug("All audio stopped via SoundPlayer");
    }

    private static void closeSplashScreen() {
        if (splashStage != null) {
            try {
                splashStage.close();
                splashStage = null;
                logger.debug("Splash screen closed");
            } catch (Exception e) {
                logger.warn("Error closing splash screen: {}", e.getMessage());
            }
        }
    }

    private void showViewModeDialogAndLoadMain(Stage primaryStage) {
        selector = new ViewModeSelector();
        String viewMode = selector.getViewMode();
        loadMainView(primaryStage, viewMode);
    }

    private void loadMainView(Stage stage, String viewMode) {
        try {
            String fxmlPath = "/com/nextinnomind/biblequizapp/views/onboarding-view.fxml";

            if ("mobile".equalsIgnoreCase(viewMode)) {
                MobileViewLoader.load(stage, fxmlPath);
                logger.info("Mobile view loaded.");
            } else {
                DesktopViewLoader.load(stage, fxmlPath);
                logger.info("Desktop view loaded.");
            }

            stage.setOnCloseRequest(event -> {
                logger.info("Stage close requested.");
                stopAllAudio();
                cleanup();
            });
        } catch (Exception e) {
            logger.error("Failed to load main view: {}", e.getMessage(), e);
            Platform.exit();
        }
    }

    @Override
    public void stop() {
        logger.info("Application is stopping...");
        stopAllAudio();
        cleanup();
    }

    private void cleanup() {
        try {
            JsonDataLoader dataLoader = JsonDataLoader.getInstance();
            dataLoader.saveAllScores();
            if (selector != null) selector.clearSavedChoice();
            logger.info("Cleanup completed successfully.");
        } catch (Exception e) {
            logger.error("Error during cleanup: {}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        logger.info("Launching JavaFX application...");
        launch(args);
    }
}
