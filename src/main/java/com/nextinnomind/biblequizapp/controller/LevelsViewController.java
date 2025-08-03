
package com.nextinnomind.biblequizapp.controller;

import com.nextinnomind.biblequizapp.Loader.DataLoader;
import com.nextinnomind.biblequizapp.Loader.ViewLoader;
import com.nextinnomind.biblequizapp.Manager.ViewModeSelectorManager;
import com.nextinnomind.biblequizapp.Style.LevelStyle;
import com.nextinnomind.biblequizapp.widget.LevelCard;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LevelsViewController {

    private static final Logger logger = LogManager.getLogger(LevelsViewController.class);

    @FXML
    private FlowPane levelContainer;

    @FXML
    private Button backButton;

    private static final int TOTAL_LEVELS = 40;
    public static int CURRENT_LEVEL = 1;
    private int highestUnlockedLevel = 1;

    private final ViewModeSelectorManager viewModeSelectorManager = new ViewModeSelectorManager();

    @FXML
    public void initialize() {
        backButton.setOnAction(_ -> handleBackAction());
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

        levelContainer.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((_, _, newWidth) -> adjustWrapLength(newWidth.doubleValue()));
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

    private void handleLevelSelected(int level) {
        CURRENT_LEVEL = level;
        if (level > highestUnlockedLevel) {
            logger.info("Level {} is locked. Cannot navigate.", CURRENT_LEVEL);
            return;
        }
        logger.info("Navigating to quiz for Level: {}", CURRENT_LEVEL);
        Stage window = (Stage) levelContainer.getScene().getWindow();
        ViewLoader.loadMainView(window, viewModeSelectorManager.getViewMode(),"/com/nextinnomind/biblequizapp/views/quiz-view.fxml");
    }

    @FXML
    private void handleBackAction() {
        Stage window = (Stage) levelContainer.getScene().getWindow();
        ViewLoader.loadMainView(window, viewModeSelectorManager.getViewMode(),"");
    }
}
