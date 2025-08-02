package com.nextinnomind.biblequizapp.AppManager;

import com.nextinnomind.biblequizapp.biblequizapp;
import com.nextinnomind.biblequizapp.AppLoader.JsonDataLoader;
import com.nextinnomind.biblequizapp.utils.JsonUtils;
import com.nextinnomind.biblequizapp.utils.SoundPlayer;
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

public class SplashScreenManager {

    private static final Logger logger = LogManager.getLogger(SplashScreenManager.class);
    private static Stage splashStage;
    private static final double SPLASH_WIDTH = 600;
    private static final double SPLASH_HEIGHT = 400;

    public static void initialize() {
        Platform.runLater(() -> {
            try {
                Thread.sleep(1000);
                showSplashScreen();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            JsonDataLoader.getInstance();

            new Thread(() -> {
                try {
                    logger.info("Removing duplicate questions...");
                    JsonUtils.removeDuplicateQuestions();

                    logger.info("Loading data...");
                    JsonDataLoader.getInstance();

                    Thread.sleep(10_000);

                    Platform.runLater(() -> {
                        AudioManager.stopAllAudio();
                        closeSplashScreen();
                    });

                } catch (Exception e) {
                    logger.error("Error during startup: {}", e.getMessage(), e);
                    Platform.exit();
                }
            }).start();
        });
    }

    private static void showSplashScreen() {
        splashStage = new Stage(StageStyle.UNDECORATED);

        StackPane splashRoot = new StackPane();
        splashRoot.setStyle("-fx-background-color: #2a2a2a;");

        Image splashImage = new Image(Objects.requireNonNull(
                biblequizapp.class.getResourceAsStream("/com/nextinnomind/biblequizapp/assets/img/splash.png")));
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
        SoundPlayer.play("/com/nextinnomind/biblequizapp/assets/sounds/splash.wav");
    }

    public static void closeSplashScreen() {
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
}