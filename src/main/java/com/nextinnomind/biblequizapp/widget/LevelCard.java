package com.nextinnomind.biblequizapp.widget;

import com.nextinnomind.biblequizapp.Loader.DataLoader;
import com.nextinnomind.biblequizapp.Style.LevelStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LevelCard {

    private static final Logger logger = LogManager.getLogger(LevelCard.class);

    public static VBox buildLevelCard(int level, int highestUnlockedLevel, Runnable onLevelSelected) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(100);
        card.setPrefHeight(160);
        card.setCursor(Cursor.HAND);

        boolean isLocked = level > highestUnlockedLevel;
        String color = LevelStyle.LEVEL_COLORS[(level - 1) % LevelStyle.LEVEL_COLORS.length];
        String bgColor = isLocked ? "#5D3A00" : color;

        card.setStyle("-fx-background-color: " + bgColor + "; " + LevelStyle.CARD_STYLE);

        HBox stars = new HBox(4);
        stars.setAlignment(Pos.CENTER);
        int earnedStars = DataLoader.getInstance().getStarsForLevel(level);

        for (int i = 0; i < 3; i++) {
            Polygon star = Star.createStar();
            star.setFill(i < earnedStars ? Color.GOLD : Color.WHITE);
            stars.getChildren().add(star);
        }

        StackPane badge = new StackPane();
        badge.setPrefSize(60, 60);
        badge.setStyle(LevelStyle.BADGE_STYLE);

        Text levelText = new Text(String.valueOf(level));
        levelText.setFill(Color.WHITE);
        levelText.setFont(Font.font("Arial", 20));
        badge.getChildren().add(levelText);

        Button playButton = new Button("Level " + level);
        playButton.setPrefWidth(80);

        if (isLocked) {
            playButton.setStyle(LevelStyle.PLAY_BUTTON_LOCKED);
            playButton.setDisable(true);
        } else {
            playButton.setStyle(LevelStyle.PLAY_BUTTON_UNLOCKED);
            playButton.setOnAction(e -> onLevelSelected.run());
        }

        card.getChildren().addAll(stars, badge, playButton);

        if (isLocked) {
            StackPane lockOverlay = new StackPane();
            lockOverlay.setStyle(LevelStyle.LOCK_OVERLAY);
            lockOverlay.setPrefSize(100, 160);

            VBox lockContent = new VBox(4);
            lockContent.setAlignment(Pos.CENTER);

            StackPane lockCircle = new StackPane();
            lockCircle.setPrefSize(180, 180);
            lockCircle.setStyle(LevelStyle.LOCK_CIRCLE);

            Text lockText = new Text("\uD83D\uDD12");
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
}
