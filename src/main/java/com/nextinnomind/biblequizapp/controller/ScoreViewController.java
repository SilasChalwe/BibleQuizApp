package com.nextinnomind.biblequizapp.controller;

import com.nextinnomind.biblequizapp.model.LevelScore;
import com.nextinnomind.biblequizapp.utils.DesktopViewLoader;
import com.nextinnomind.biblequizapp.utils.JsonDataLoader;
import com.nextinnomind.biblequizapp.utils.MobileViewLoader;
import com.nextinnomind.biblequizapp.utils.ViewModeSelector;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.nextinnomind.biblequizapp.controller.LevelsViewController.CURRENT_LEVEL;

public class ScoreViewController {

    private static final Logger logger = LogManager.getLogger(ScoreViewController.class);
    private static final double PASS_THRESHOLD = 50.0; // 50% to pass and unlock next level
    public Label quiz_level_name;

    @FXML private Label trophyIcon;
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
    private final ViewModeSelector viewModeSelector = new ViewModeSelector();
    private LevelScore currentScore;
    private boolean isPassed;
    private boolean wasScoreSaved; // Track if score was saved to determine next level availability

    @FXML
    public void initialize() {
        setupEventHandlers();
        loadScoreData();
        setupUI();
        animateScoreDisplay();
    }

    private void setupEventHandlers() {
        retryButton.setOnAction(e -> handleRetry());
        nextLevelButton.setOnAction(e -> handleNextLevel());
        levelsButton.setOnAction(e -> handleLevels());

        // Add hover effects
        addButtonHoverEffects();
    }

    private void loadScoreData() {
        // current quiz score
        this.currentScore = QuizViewController.CURRENT_QUIZ_SCORE;
        quiz_level_name.setText("Level " + CURRENT_LEVEL+" Score");

        // If no current quiz score, fallback to saved score
        if (currentScore == null) {
            this.currentScore = JsonDataLoader.getInstance().getLatestScore(CURRENT_LEVEL);
        }

        // Last resort fallback
        if (currentScore == null) {
            currentScore = new LevelScore();
            currentScore.setLevel(CURRENT_LEVEL);
            currentScore.setScore(0);
            currentScore.setCorrectAnswers(0);
            currentScore.setTotalQuestions(15);
            logger.warn("No score data found, using fallback score");
        }

        //check if player is passed
        double percentage = currentScore.getScore();
        isPassed = percentage >= PASS_THRESHOLD;

        // Check if score was actually saved
        LevelScore savedScore = JsonDataLoader.getInstance().getLatestScore(CURRENT_LEVEL);
        wasScoreSaved = savedScore != null && savedScore.getScore() >= PASS_THRESHOLD;

        logger.info("Loaded score data: Level {}, Score: {}, Passed: {}, Saved: {}",
                CURRENT_LEVEL, String.format("%.1f", percentage), isPassed, wasScoreSaved);
    }

    private void setupUI() {
        // Set basic score info
        correctAnswers.setText(String.valueOf(currentScore.getCorrectAnswers()));

        int incorrect = currentScore.getTotalQuestions() - currentScore.getCorrectAnswers();
        incorrectAnswers.setText(String.valueOf(incorrect));
        totalQuestions.setText(String.valueOf(currentScore.getTotalQuestions()));

        double percentage = currentScore.getScore();
        scorePercentage.setText(String.format("%.0f%%", percentage));

        // Setup UI based on pass/fail and whether score was saved
        if (isPassed && wasScoreSaved) {
            setupPassedUI();
            setupButtonsForPassed();
        } else if (isPassed) {
            // Edge case: passed now but wasn't saved before
            setupPassedUI();
            setupButtonsForPassed();
        } else {
            setupFailedUI();
            setupButtonsForFailed();
        }
    }

    private void setupPassedUI() {
        // Trophy and success styling
        trophyIcon.setText("🏆");
        trophyIcon.setStyle("-fx-font-size: 50px; -fx-text-fill: #FFD700;");
        trophyBackground.setStyle("-fx-fill: radial-gradient(center 50% 50%, radius 60%, rgba(255,215,0,0.2), rgba(255,215,0,0.05)); -fx-stroke: #FFD700; -fx-stroke-width: 3;");

        // Success messages
        String[] successTitles = {
                "Congratulations!",
                "Excellent Work!",
                "Outstanding!",
                "Well Done!"
        };

        String[] successMessages = {
                "You've successfully completed this level! Next level unlocked!",
                "Your knowledge is impressive! Ready for the next challenge?",
                "You're on fire! The next level awaits you!",
                "Fantastic performance! You've earned access to the next level!"
        };

        int randomIndex = (int) (Math.random() * successTitles.length);
        resultTitle.setText(successTitles[randomIndex]);
        resultMessage.setText(successMessages[randomIndex]);

        // Success colors
        resultTitle.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 28px; -fx-font-weight: bold; -fx-font-family: 'Palatino Linotype';");
    }

    private void setupFailedUI() {
        // Failure
        trophyIcon.setText("💔");
        trophyIcon.setStyle("-fx-font-size: 50px; -fx-text-fill: #e74c3c;");
        trophyBackground.setStyle("-fx-fill: radial-gradient(center 50% 50%, radius 60%, rgba(231,76,60,0.2), rgba(231,76,60,0.05)); -fx-stroke: #e74c3c; -fx-stroke-width: 3;");

        // Motivational failure messages with threshold information
        String[] failureTitles = {
                "Keep Trying! ",
                "Almost There! ",
                "Don't Give Up! ",
                "Practice Makes Perfect! "
        };

        String[] failureMessages = {
                "You need 50% or higher to unlock the next level. Keep practicing!",
                "You're getting stronger with each attempt! Aim for 50% to advance!",
                "Success is just around the corner. Score 50% to unlock the next level!",
                "Every expert was once a beginner. Get 50% to move forward!"
        };

        int randomIndex = (int) (Math.random() * failureTitles.length);
        resultTitle.setText(failureTitles[randomIndex]);
        resultMessage.setText(failureMessages[randomIndex]);

        // Failure colors - will be set dynamically in animation
        resultTitle.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 28px; -fx-font-weight: bold; -fx-font-family: 'Palatino Linotype';");
    }

    private void setupButtonsForPassed() {
        // User passed and score was saved - show next level button
        retryButton.setVisible(false);
        nextLevelButton.setVisible(true);
        levelsButton.setVisible(true);
    }

    private void setupButtonsForFailed() {
        // User failed - only show retry and levels buttons
        retryButton.setVisible(true);
        nextLevelButton.setVisible(false);
        levelsButton.setVisible(true);
    }
    private void animateScoreDisplay() {
        double radius = 45;
        double circumference = 2 * Math.PI * radius;
        double percentage = currentScore.getScore();

        System.out.println("Debug - Score percentage: " + percentage); // Debug line

        // Clear any existing dash array first
        scoreProgressCircle.getStrokeDashArray().clear();

        // Setup stroke dash array
        scoreProgressCircle.getStrokeDashArray().setAll(circumference, circumference);

        // Start from top (12 o'clock)
        scoreProgressCircle.setRotate(90); // Start from 12 o'clock position
        scoreProgressCircle.setScaleX(-1); // Flip horizontally for anticlockwise direction

        // Set stroke width to make it visible
        scoreProgressCircle.setStrokeWidth(6);

        // Initial state: no progress shown (full dash offset)
        scoreProgressCircle.setStrokeDashOffset(circumference);

        // Calculate final dash offset based on percentage
        double finalDashOffset = circumference - (circumference * percentage / 100.0);

        // Set color based on score threshold
        javafx.scene.paint.Color strokeColor;
        if (percentage < PASS_THRESHOLD) {
            // Red for failing scores
            strokeColor = javafx.scene.paint.Color.web("#e74c3c");
        } else {
            // Green for passing scores
            strokeColor = javafx.scene.paint.Color.web("#27ae60");
        }

        // Set the stroke color immediately
        scoreProgressCircle.setStroke(strokeColor);

        // Make sure the circle is visible
        scoreProgressCircle.setVisible(true);
        scoreProgressCircle.setOpacity(1.0);

        System.out.println("Debug - Final dash offset: " + finalDashOffset); // Debug line
        System.out.println("Debug - Circumference: " + circumference); // Debug line

        // Create animation timeline
        Timeline timeline = new Timeline();

        // Add dash offset animation
        KeyValue dashValue = new KeyValue(scoreProgressCircle.strokeDashOffsetProperty(), finalDashOffset);
        KeyFrame dashFrame = new KeyFrame(Duration.seconds(1.5), dashValue);
        timeline.getKeyFrames().add(dashFrame);

        // Add debug to see if animation starts
        timeline.setOnFinished(e -> System.out.println("Animation completed"));

        timeline.play();

        // Animate trophy appearance
        Platform.runLater(() -> {
            Timeline trophyAnimation = new Timeline();
            trophyIcon.setScaleX(0);
            trophyIcon.setScaleY(0);

            KeyValue scaleX = new KeyValue(trophyIcon.scaleXProperty(), 1.0);
            KeyValue scaleY = new KeyValue(trophyIcon.scaleYProperty(), 1.0);
            KeyFrame trophyFrame = new KeyFrame(Duration.seconds(0.5), scaleX, scaleY);

            trophyAnimation.getKeyFrames().add(trophyFrame);
            trophyAnimation.setDelay(Duration.seconds(0.3));
            trophyAnimation.play();
        });
    }

    private void addButtonHoverEffects() {
        // Retry button hover
        retryButton.setOnMouseEntered(e ->
                retryButton.setStyle(retryButton.getStyle() + "; -fx-background-color: #c0392b;"));
        retryButton.setOnMouseExited(e ->
                retryButton.setStyle(retryButton.getStyle().replace("; -fx-background-color: #c0392b;", "")));

        // Next level button hover
        nextLevelButton.setOnMouseEntered(e ->
                nextLevelButton.setStyle(nextLevelButton.getStyle() + "; -fx-background-color: #229954;"));
        nextLevelButton.setOnMouseExited(e ->
                nextLevelButton.setStyle(nextLevelButton.getStyle().replace("; -fx-background-color: #229954;", "")));

        // Levels button hover
        levelsButton.setOnMouseEntered(e ->
                levelsButton.setStyle(levelsButton.getStyle() + "; -fx-background-color: #5a67d8;"));
        levelsButton.setOnMouseExited(e ->
                levelsButton.setStyle(levelsButton.getStyle().replace("; -fx-background-color: #5a67d8;", "")));
    }

    @FXML
    private void handleRetry() {
        logger.info("Retrying level {}", CURRENT_LEVEL);
        navigateToQuiz();
    }

    @FXML
    private void handleNextLevel() {
        // Only allow if score was actually saved (meaning they passed with 50%+)
        if (!wasScoreSaved) {
            logger.warn("Attempted to access next level without passing current level");
            return;
        }

        // Navigate to next level
        int nextLevel = CURRENT_LEVEL + 1;
        logger.info("Navigating to next level: {}", nextLevel);

        // Update current level and navigate
        LevelsViewController.CURRENT_LEVEL = nextLevel;
        navigateToQuiz();
    }

    @FXML
    private void handleLevels() {
        logger.info("Navigating to levels view");
        navigateToLevels();
    }

    @FXML
    private void handleBack() {
        logger.info("Navigating back to levels");
        navigateToLevels();
    }

    private void navigateToQuiz() {
        try {
            Stage stage = (Stage) levelsButton.getScene().getWindow();
            String quizViewPath = "/com/nextinnomind/biblequizapp/views/quiz-view.fxml";
            String viewMode = viewModeSelector.getViewMode();

            if ("mobile".equalsIgnoreCase(viewMode)) {
                MobileViewLoader.load(stage, quizViewPath);
            } else {
                DesktopViewLoader.load(stage, quizViewPath);
            }
        } catch (IOException e) {
            logger.error("Failed to load quiz view: {}", e.getMessage(), e);
        }
    }

    private void navigateToLevels() {
        try {
            Stage stage = (Stage) levelsButton.getScene().getWindow();
            String levelsViewPath = "/com/nextinnomind/biblequizapp/views/levels-view.fxml";
            String viewMode = viewModeSelector.getViewMode();

            if ("mobile".equalsIgnoreCase(viewMode)) {
                MobileViewLoader.load(stage, levelsViewPath);
            } else {
                DesktopViewLoader.load(stage, levelsViewPath);
            }
        } catch (IOException e) {
            logger.error("Failed to load levels view: {}", e.getMessage(), e);
        }
    }
}