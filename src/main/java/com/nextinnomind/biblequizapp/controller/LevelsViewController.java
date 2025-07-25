package com.nextinnomind.biblequizapp.controller;

import com.nextinnomind.biblequizapp.utils.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LevelsViewController {

    private static final Logger logger = LogManager.getLogger(LevelsViewController.class);

    @FXML
    private FlowPane levelContainer;

    @FXML
    private Button backButton;

    private static final int TOTAL_LEVELS = 40;
    public static int CURRENT_LEVEL = 1;
    private int highestUnlockedLevel = 1;

    private final ViewModeSelector viewModeSelector = new ViewModeSelector();

    @FXML
    public void initialize() {
        backButton.setOnAction(e -> handleBackAction());
        levelContainer.setHgap(15);
        levelContainer.setVgap(15);
        levelContainer.setPadding(new Insets(20));
        levelContainer.setAlignment(Pos.CENTER);
        levelContainer.setStyle("-fx-background-color: #1E144F");
        this.highestUnlockedLevel += JsonDataLoader.getInstance().getTotalLevelsCount() ;


        // Load level cards
        for (int i = 1; i <= TOTAL_LEVELS; i++) {
            VBox levelCard = buildLevelCard(i);
            levelContainer.getChildren().add(levelCard);
        }

        // Responsive wrap length adjustment based on scene width
        levelContainer.sceneProperty().addListener((obsScene, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((obsWidth, oldWidth, newWidth) -> adjustWrapLength(newWidth.doubleValue()));
                adjustWrapLength(newScene.getWidth());
            }
        });

        logger.info("LevelsViewController initialized with highestUnlockedLevel={}", highestUnlockedLevel);
    }

    private void adjustWrapLength(double windowWidth) {
        double wrapLength = windowWidth * 0.8;
        wrapLength = Math.max(wrapLength, 350);
        wrapLength = Math.min(wrapLength, 900);
        levelContainer.setPrefWrapLength(wrapLength);
        logger.info("Adjusted FlowPane prefWrapLength to: {}", wrapLength);
    }


private VBox buildLevelCard(int level) {
    VBox card = new VBox(8);
    card.setPadding(new Insets(10));
    card.setAlignment(Pos.CENTER);
    card.setPrefWidth(100);
    card.setPrefHeight(160);
    card.setCursor(Cursor.HAND);

    boolean isLocked = level > highestUnlockedLevel;

    // Updated color palette (no gold/yellow tones)
    String[] levelColors = {
            "#6BCB77", "#4D96FF", "#A66DD4", "#FF6B6B",
            "#00C2D1", "#FF7B9C", "#FF8C42", "#ADFF2F",
            "#6F38C5", "#0FC2C0", "#9C27B0", "#3F51B5"
    };
    String color = levelColors[(level - 1) % levelColors.length];
    String bgColor = isLocked ? "#5D3A00" : color;

    card.setStyle("-fx-background-color: " + bgColor + "; -fx-border-radius: 10; -fx-background-radius: 10;");

    HBox stars = new HBox(4);
    stars.setAlignment(Pos.CENTER);
    int earnedStars = JsonDataLoader.getInstance().getStarsForLevel(level);

    for (int i = 0; i < 3; i++) {
        Polygon star = createStar();
        star.setFill(i < earnedStars ? Color.GOLD : Color.WHITE);
        stars.getChildren().add(star);
    }

    StackPane badge = new StackPane();
    badge.setPrefSize(60, 60);
    badge.setStyle(
            "-fx-background-radius: 30;" +
                    "-fx-background-color: linear-gradient(to bottom right, #3A3A3A, #1E1E1E);" +
                    "-fx-border-radius: 30;" +
                    "-fx-border-color: white;" +
                    "-fx-border-width: 2;"
    );

    Text levelText = new Text(String.valueOf(level));
    levelText.setFill(Color.WHITE);
    levelText.setFont(Font.font("Arial", 20));
    badge.getChildren().add(levelText);

    Button playButton = new Button("Level " + level);
    playButton.setPrefWidth(80);

    if (isLocked) {
        playButton.setStyle("-fx-background-color: #4B0082; -fx-text-fill: white;");
        playButton.setDisable(true);
    } else {
        playButton.setStyle("-fx-background-color: #6A11CB; -fx-text-fill: white;");
        playButton.setOnAction(e -> handleLevelSelected(level));
    }

    card.getChildren().addAll(stars, badge, playButton);

    if (isLocked) {
        StackPane lockOverlay = new StackPane();
        lockOverlay.setStyle(
                "-fx-background-color: rgba(75, 0, 130, 0.75);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;"
        );
        lockOverlay.setPrefSize(100, 160);

        VBox lockContent = new VBox(4);
        lockContent.setAlignment(Pos.CENTER);

        StackPane lockCircle = new StackPane();
        lockCircle.setPrefSize(180, 180);
        lockCircle.setStyle(
                "-fx-background-radius: 9;" +
                        "-fx-background-color: rgba(186, 85, 211, 0.25);"
        );

        Text lockText = new Text("\uD83D\uDD12"); // 🔒
        lockText.setFont(Font.font(50));
        lockText.setFill(Color.BLACK);
        lockCircle.getChildren().add(lockText);

        Text lockLabel = new Text("Locked");
        lockLabel.setFill(Color.WHITE);
        lockLabel.setFont(Font.font(12));

        lockContent.getChildren().addAll(lockCircle, lockLabel);
        lockOverlay.getChildren().add(lockContent);

        StackPane wrapper = new StackPane(card, lockOverlay);
        wrapper.setPrefSize(100, 160);
        wrapper.setCursor(Cursor.DEFAULT);

        wrapper.setOnMouseClicked(e -> {
            logger.info("Level {} is locked.", level);
            e.consume();
        });

        return new VBox(wrapper);
    }

    return card;
}

    @NotNull
    private Polygon createStar() {
        Polygon star = new Polygon();
        int spikes = 5;
        double innerRadius = 3.5;

        for (int i = 0; i < spikes * 2; i++) {
            double angle = Math.PI / spikes * i;
            double r = (i % 2 == 0) ? 7 : innerRadius;
            double x = 7 + Math.cos(angle - Math.PI / 2) * r;
            double y = 7 + Math.sin(angle - Math.PI / 2) * r;
            star.getPoints().addAll(x, y);
        }

        return star;
    }

    private void handleLevelSelected(int level) {
        CURRENT_LEVEL = level;
        if (level > highestUnlockedLevel) {
            logger.info("Level {} is locked. Cannot navigate.", CURRENT_LEVEL);
            return;
        }
        logger.info("Navigating to quiz for Level: {}", CURRENT_LEVEL);

        try {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            String fxmlPath = "/com/nextinnomind/biblequizapp/views/quiz-view.fxml";
            String viewMode = viewModeSelector.getViewMode();

            if ("mobile".equalsIgnoreCase(viewMode)) {
                MobileViewLoader.load(currentStage, fxmlPath);
            } else {
                DesktopViewLoader.load(currentStage, fxmlPath);
            }
        } catch (IOException e) {
            logger.error("Can't load the UI FXML for level {}: {}", CURRENT_LEVEL, e.getMessage());
            //e.printStackTrace();
        }
    }

    @FXML
    private void handleBackAction() {
        logger.info("Back button clicked");
        try {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            String viewMode = viewModeSelector.getViewMode();
            if ("mobile".equalsIgnoreCase(viewMode)) {
                MobileViewLoader.load(currentStage, "/com/nextinnomind/biblequizapp/views/onboarding-view.fxml");
            } else {
                DesktopViewLoader.load(currentStage, "/com/nextinnomind/biblequizapp/views/onboarding-view.fxml");
            }
        } catch (IOException e) {
            logger.error("Can't load the onboarding UI FXML: {}", e.getMessage());
        }
    }
}
