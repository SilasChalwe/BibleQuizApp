package com.nextinnomind.biblequizapp.controller;

import com.nextinnomind.biblequizapp.utils.JsonDataLoader;
import com.nextinnomind.biblequizapp.utils.MobileViewLoader;
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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static java.awt.Color.red;


public class LevelsController {

    @FXML
    private FlowPane levelContainer;

    @FXML
    private Button backButton;

    private static final int TOTAL_LEVELS = 12;
    public static int CURRENT_LEVEL = 1;
    private int highestUnlockedLevel = 1;


    @FXML
    public void initialize() {
        backButton.setOnAction(e -> handleBackAction());
        levelContainer.setHgap(15);
        levelContainer.setVgap(15);
        levelContainer.setPadding(new Insets(20));
        levelContainer.setAlignment(Pos.CENTER);
        levelContainer.setPrefWrapLength(350);
        levelContainer.setStyle("-fx-background-color: #1E144F");

        this.highestUnlockedLevel = JsonDataLoader.openedLevels.size();


        for (int i = 1; i <= TOTAL_LEVELS; i++) {
            VBox levelCard = buildLevelCard(i);
            levelContainer.getChildren().add(levelCard);
        }

    }

    private VBox buildLevelCard(int level) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(100);
        card.setPrefHeight(160);
        card.setCursor(Cursor.HAND);
        //card.setStyle("-fx-background-color:red");

        boolean isLocked = level > highestUnlockedLevel;
        String bgColor = isLocked ? "#5D3A00" : "#1E146F";
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
                        "-fx-background-color: linear-gradient(to bottom right, #FFD700, #FFB300);" +
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
                System.out.println("Level " + level + " is locked.");
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
        double innerRadius = (double) 7 * 0.5;

        for (int i = 0; i < spikes * 2; i++) {
            double angle = Math.PI / spikes * i;
            double r = (i % 2 == 0) ? (double) 7 : innerRadius;
            double x = (double) 7 + Math.cos(angle - Math.PI / 2) * r;
            double y = (double) 7 + Math.sin(angle - Math.PI / 2) * r;
            star.getPoints().addAll(x, y);
        }

        return star;
    }

    private void handleLevelSelected(int level) {
        CURRENT_LEVEL = level;
        if (level > highestUnlockedLevel) {
            System.out.println("Level " + CURRENT_LEVEL + " is locked. Cannot navigate.");
            return;
        }

        System.out.println("Navigating to quiz for Level: " + CURRENT_LEVEL);

        try {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            String fxmlPath = String.format("/com/nextinnomind/biblequizapp/views/quiz-level-%d.fxml", 1);
            MobileViewLoader.load(currentStage, fxmlPath);
        } catch (IOException e) {
            System.out.println("Can't load the UI FXML for level " + CURRENT_LEVEL);

        }
    }

    @FXML
    private void handleBackAction() {
        System.out.println("Back button clicked");
        try {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            MobileViewLoader.load(currentStage, "/com/nextinnomind/biblequizapp/views/onboarding-view.fxml");
        } catch (IOException e) {
            System.out.println("Can't load the onboarding UI FXML");

        }
    }

}
