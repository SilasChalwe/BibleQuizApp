package com.nextinnomind.biblequizapp.controller;

import com.nextinnomind.biblequizapp.Display.DesktopDisplay;
import com.nextinnomind.biblequizapp.Display.MobileDisplay;
import com.nextinnomind.biblequizapp.Loader.ScoreLoader;
import com.nextinnomind.biblequizapp.Manager.ViewModeSelectorManager;
import com.nextinnomind.biblequizapp.model.LevelScore;
import com.nextinnomind.biblequizapp.utils.ScoreMessageHelper;
import com.nextinnomind.biblequizapp.widget.ScoreAnimator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.nextinnomind.biblequizapp.controller.LevelsViewController.CURRENT_LEVEL;

public class ScoreViewController {
    private static final Logger logger = LogManager.getLogger(ScoreViewController.class);

    // FXML Components
    @FXML private Label quiz_level_name;
    @FXML private ImageView trophyImage;
    @FXML private Circle trophyBackground;
    @FXML private Label resultTitle;
    @FXML private Label resultMessage;
    @FXML private Circle scoreProgressCircle;
    @FXML private Label scorePercentage;
    @FXML private Label correctAnswers;
    @FXML private Label incorrectAnswers;
    @FXML private Label totalQuestions;
    @FXML private Button retryButton;
    @FXML private Button nextLevelButton;
    @FXML private Button levelsButton;

    // Helper classes
    private final ScoreLoader scoreLoader = new ScoreLoader();
    private final ScoreMessageHelper messageHelper = new ScoreMessageHelper();
    private final ScoreAnimator animator = new ScoreAnimator();
    private final ViewModeSelectorManager viewModeSelectorManager = new ViewModeSelectorManager();

    @FXML
    public void initialize() {
        quiz_level_name.setText("Level " + CURRENT_LEVEL + " Score");
        scoreLoader.loadScoreData();

        setupUI();
        setupEventHandlers();
        animator.animateScoreDisplay(scoreProgressCircle, scoreLoader.getCurrentScore().getScore(), trophyImage);
    }

    private void setupUI() {
        updateScoreLabels();
        configureUIForResult();
    }

    private void updateScoreLabels() {
        LevelScore currentScore = scoreLoader.getCurrentScore();
        correctAnswers.setText(String.valueOf(currentScore.getCorrectAnswers()));

        int incorrect = currentScore.getTotalQuestions() - currentScore.getCorrectAnswers();
        incorrectAnswers.setText(String.valueOf(incorrect));
        totalQuestions.setText(String.valueOf(currentScore.getTotalQuestions()));

        double percentage = currentScore.getScore();
        scorePercentage.setText(String.format("%.0f%%", percentage));
    }

    private void configureUIForResult() {
        if (scoreLoader.isPassed()) {
            messageHelper.configureSuccessUI(trophyImage, trophyBackground, resultTitle, resultMessage);
            setupButtonsForPassed();
        } else {
            messageHelper.configureFailureUI(trophyImage, trophyBackground, resultTitle, resultMessage);
            setupButtonsForFailed();
        }
    }

    private void setupButtonsForPassed() {
        retryButton.setVisible(false);
        nextLevelButton.setVisible(true);
        levelsButton.setVisible(true);
    }

    private void setupButtonsForFailed() {
        retryButton.setVisible(true);
        nextLevelButton.setVisible(false);
        levelsButton.setVisible(true);
    }

    private void setupEventHandlers() {
        retryButton.setOnAction(e -> handleRetry());
        nextLevelButton.setOnAction(e -> handleNextLevel());
        levelsButton.setOnAction(e -> handleLevels());
        addButtonHoverEffects();
    }

    private void addButtonHoverEffects() {
        addHoverEffect(retryButton, "#c0392b");
        addHoverEffect(nextLevelButton, "#229954");
        addHoverEffect(levelsButton, "#5a67d8");
    }

    private void addHoverEffect(Button button, String hoverColor) {
        String hoverStyle = "; -fx-background-color: " + hoverColor + ";";
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace(hoverStyle, "")));
    }

    @FXML
    private void handleRetry() {
        logger.info("Retrying level {}", CURRENT_LEVEL);
        navigateToQuiz();
    }

    @FXML
    private void handleNextLevel() {
        if (!scoreLoader.wasScoreSaved()) {
            logger.warn("Attempted to access next level without passing current level");
            return;
        }

        int nextLevel = CURRENT_LEVEL + 1;
        logger.info("Navigating to next level: {}", nextLevel);
        CURRENT_LEVEL = nextLevel;
        navigateToQuiz();
    }

    @FXML
    private void handleLevels() {
        logger.info("Navigating to levels view");
        navigateToLevels();
    }

    private void navigateToQuiz() {
        navigateToView("/com/nextinnomind/biblequizapp/views/quiz-view.fxml", "quiz view");
    }

    private void navigateToLevels() {
        navigateToView("/com/nextinnomind/biblequizapp/views/levels-view.fxml", "levels view");
    }

    private void navigateToView(String viewPath, String viewName) {
        try {
            Stage stage = (Stage) levelsButton.getScene().getWindow();
            String viewMode = viewModeSelectorManager.getViewMode();

            if ("mobile".equalsIgnoreCase(viewMode)) {
                MobileDisplay.load(stage, viewPath);
            } else {
                DesktopDisplay.load(stage, viewPath);
            }
        } catch (IOException e) {
            logger.error("Failed to load {}: {}", viewName, e.getMessage(), e);
        }
    }
}