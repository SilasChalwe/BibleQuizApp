
package com.nextinnomind.biblequizapp.controller;


import com.nextinnomind.biblequizapp.Loader.DataLoader;
import com.nextinnomind.biblequizapp.Loader.ViewLoader;
import com.nextinnomind.biblequizapp.Manager.ViewModeSelectorManager;
import com.nextinnomind.biblequizapp.Style.LevelStyle;
import com.nextinnomind.biblequizapp.widget.LevelCard;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class LevelsViewController {

    private static final Logger logger = LogManager.getLogger(LevelsViewController.class);
    public Button exit;

    @FXML
    private FlowPane levelContainer;

    @FXML
    private Button backButton;

    private static final int TOTAL_LEVELS = 12;
    public static int CURRENT_LEVEL = 1;
    private int highestUnlockedLevel = 1;

    @FXML private ScrollPane scrollPane;


    @FXML public void initialize() {
        backButton.setOnAction(_ -> handleBackAction());
        exit.setOnAction(_ -> showExitConfirmationDialog());
        levelContainer.setHgap(15);
        levelContainer.setVgap(15);
        levelContainer.setPadding(new Insets(20));
        levelContainer.setAlignment(Pos.CENTER);
        levelContainer.setStyle(LevelStyle.BACKGROUND_COLOR);

        this.highestUnlockedLevel += DataLoader.getInstance().getTotalLevelsCount();

        for (int i = 1; i <= TOTAL_LEVELS; i++) {
            int finalLevel = i;
            levelContainer.getChildren().add(
                    LevelCard.buildLevelCard(finalLevel, highestUnlockedLevel, () -> handleLevelSelected(finalLevel))
            );
        }

        scrollPane.viewportBoundsProperty().addListener((_, _, newBounds) -> {
            if (newBounds != null) {
                adjustWrapLength(newBounds.getWidth());
            }
        });

        if (scrollPane.getViewportBounds() != null) {
            adjustWrapLength(scrollPane.getViewportBounds().getWidth());
        }

        logger.info("LevelsViewController initialized with highestUnlockedLevel={}", highestUnlockedLevel);
    }

    private void adjustWrapLength(double availableWidth) {
        // Fallback for invalid width
        if (availableWidth <= 0) {
            availableWidth = 350; // default min width
        }

        double wrapLength = availableWidth * 0.8;
        wrapLength = Math.max(wrapLength, 350);
        wrapLength = Math.min(wrapLength, 900);

        levelContainer.setPrefWrapLength(wrapLength);
        logger.info("Adjusted FlowPane prefWrapLength to: {}", wrapLength);
    }


    private void handleLevelSelected(int level) {
        CURRENT_LEVEL = level;
        if (level > highestUnlockedLevel) {
            logger.info("Level {} is locked. Cannot navigate.", CURRENT_LEVEL);
            return;
        }
        logger.info("Navigating to quiz for Level: {}", CURRENT_LEVEL);
        Stage window = (Stage) levelContainer.getScene().getWindow();
        ViewLoader.loadMainView(window, ViewModeSelectorManager.getViewMode(),"/com/nextinnomind/biblequizapp/views/quiz-view.fxml");
    }

    @FXML
    private void handleBackAction() {
        Stage window = (Stage) levelContainer.getScene().getWindow();
        ViewLoader.loadMainView(window, ViewModeSelectorManager.getViewMode(),"");
    }


    private void showExitConfirmationDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED); // borderless

        Label message = new Label("Are you sure you want to exit?");
        message.setFont(Font.font(18));
        message.setTextFill(Color.WHITE);

        VBox layout = getVBox(dialog, message);
        layout.setStyle("-fx-background-color: #6a0dad; -fx-padding: 30px; -fx-border-radius: 10px;");
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 180);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    @NotNull
    private static VBox getVBox(Stage dialog, Label message) {
        Button yesButton = new Button("Yes");
        yesButton.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-size: 14px;");
        yesButton.setOnAction(_ -> {
            dialog.close();
            Platform.exit();
            System.exit(0);
        });

        Button noButton = new Button("Cancel");
        noButton.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #8e44ad; -fx-font-size: 14px;");
        noButton.setOnAction(_ -> dialog.close());

        return new VBox(15, message, yesButton, noButton);
    }




}
