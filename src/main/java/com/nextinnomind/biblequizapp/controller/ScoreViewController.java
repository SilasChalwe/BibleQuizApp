package com.nextinnomind.biblequizapp.controller;

import com.nextinnomind.biblequizapp.AppLoader.DesktopViewLoader;
import com.nextinnomind.biblequizapp.AppLoader.ImageLoader;
import com.nextinnomind.biblequizapp.AppLoader.JsonDataLoader;
import com.nextinnomind.biblequizapp.AppLoader.MobileViewLoader;
import com.nextinnomind.biblequizapp.AppManager.ViewModeSelectorManager;
import com.nextinnomind.biblequizapp.model.LevelScore;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.nextinnomind.biblequizapp.controller.LevelsViewController.CURRENT_LEVEL;

public class ScoreViewController {

    private static final Logger logger = LogManager.getLogger(ScoreViewController.class);
    private static final double PASS_THRESHOLD = 50.0;
    private static final double ANIMATION_DURATION = 1.5;
    private static final double TROPHY_ANIMATION_DELAY = 0.3;
    private static final double TROPHY_ANIMATION_DURATION = 0.5;


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

    // Dependencies and state
    private final ViewModeSelectorManager viewModeSelectorManager = new ViewModeSelectorManager();
    private final JsonDataLoader dataLoader = JsonDataLoader.getInstance();
    private LevelScore currentScore;
    private boolean isPassed;
    private boolean wasScoreSaved;

    @FXML
    public void initialize() {
        loadScoreData();
        setupUI();
        setupEventHandlers();
        animateScoreDisplay();
    }

    private void loadScoreData() {
        loadCurrentScore();
        calculatePassStatus();
        checkSavedScore();
        logScoreInfo();
    }

    private void loadCurrentScore() {
        currentScore = QuizViewController.CURRENT_QUIZ_SCORE;
        quiz_level_name.setText("Level " + CURRENT_LEVEL + " Score");

        if (currentScore == null) {
            currentScore = dataLoader.getLatestScore(CURRENT_LEVEL);
        }

        if (currentScore == null) {
            currentScore = createFallbackScore();
            logger.warn("No score data found, using fallback score");
        }
    }

    private LevelScore createFallbackScore() {
        LevelScore fallbackScore = new LevelScore();
        fallbackScore.setLevel(CURRENT_LEVEL);
        fallbackScore.setScore(0);
        fallbackScore.setCorrectAnswers(0);
        fallbackScore.setTotalQuestions(15);
        return fallbackScore;
    }

    private void calculatePassStatus() {
        double percentage = currentScore.getScore();
        isPassed = percentage >= PASS_THRESHOLD;
    }

    private void checkSavedScore() {
        LevelScore savedScore = dataLoader.getLatestScore(CURRENT_LEVEL);
        wasScoreSaved = savedScore != null && savedScore.getScore() >= PASS_THRESHOLD;
    }

    private void logScoreInfo() {
        logger.info("Loaded score data: Level {}, Score: {}, Passed: {}, Saved: {}",
                CURRENT_LEVEL, String.format("%.1f", currentScore.getScore()), isPassed, wasScoreSaved);
    }

    private void setupUI() {
        ScoreLabels();
        ShowUIBasedOnResult();
    }

    private void ScoreLabels() {
        correctAnswers.setText(String.valueOf(currentScore.getCorrectAnswers()));

        int incorrect = currentScore.getTotalQuestions() - currentScore.getCorrectAnswers();
        incorrectAnswers.setText(String.valueOf(incorrect));
        totalQuestions.setText(String.valueOf(currentScore.getTotalQuestions()));

        double percentage = currentScore.getScore();
        scorePercentage.setText(String.format("%.0f%%", percentage));
    }

    private void ShowUIBasedOnResult() {
        if (isPassed && wasScoreSaved) {
            setupPassedUI();
            setupButtonsForPassed();
        } else if (isPassed) {
            setupPassedUI();
            setupButtonsForPassed();
        } else {
            setupFailedUI();
            setupButtonsForFailed();
        }
    }

    //show the score ui based on the result
    private void setupPassedUI() {
        TrophyForSuccess();
        setSuccessMessages();
        applySuccessStyles();
    }
    private void setupFailedUI() {
        TrophyForFailure();
        setFailureMessages();
        applyFailureStyles();
    }



    private void TrophyForSuccess() {
        trophyImage.setImage(ImageLoader.imageLoad("/com/nextinnomind/biblequizapp/assets/img/praying-hand-logo.png"));
        trophyBackground.setStyle("-fx-fill: radial-gradient(center 50% 50%, radius 60%, " +
                "rgba(255,215,0,0.2), rgba(255,215,0,0.05)); -fx-stroke: #FFD700; -fx-stroke-width: 3;");
    }
    private void TrophyForFailure() {
        trophyImage.setImage(ImageLoader.imageLoad("/com/nextinnomind/biblequizapp/assets/img/praying-hand-logo.png"));
        trophyBackground.setStyle("-fx-fill: radial-gradient(center 50% 50%, radius 60%, " +
                "rgba(231,76,60,0.2), rgba(231,76,60,0.05)); -fx-stroke: #e74c3c; -fx-stroke-width: 3;");
    }



    private void setSuccessMessages() {
        String[] titles = {"Congratulations!", "Excellent Work!", "Outstanding!", "Well Done!"};
        String[] messages = {
                "You've successfully completed this level! Next level unlocked!",
                "Your knowledge is impressive! Ready for the next challenge?",
                "You're on fire! The next level awaits you!",
                "Fantastic performance! You've earned access to the next level!"
        };

        int randomIndex = (int) (Math.random() * titles.length);
        resultTitle.setText(titles[randomIndex]);
        resultMessage.setText(messages[randomIndex]);
    }
    private void setFailureMessages() {
        String[] titles = {"Keep Trying! ", "Almost There! ", "Don't Give Up! ", "Practice Makes Perfect! "};
        String[] messages = {
                "You need 50% or higher to unlock the next level. Keep practicing!",
                "You're getting stronger with each attempt! Aim for 50% to advance!",
                "Success is just around the corner. Score 50% to unlock the next level!",
                "Every expert was once a beginner. Get 50% to move forward!"
        };

        int randomIndex = (int) (Math.random() * titles.length);
        resultTitle.setText(titles[randomIndex]);
        resultMessage.setText(messages[randomIndex]);
    }


    private void applySuccessStyles() {
        resultTitle.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 28px; " +
                "-fx-font-weight: bold; -fx-font-family: 'Palatino Linotype';");
    }
    private void applyFailureStyles() {
        resultTitle.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 28px; " +
                "-fx-font-weight: bold; -fx-font-family: 'Palatino Linotype';");
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

    private void animateScoreDisplay() {
        animateProgressCircle();
        animateTrophyAppearance();
    }

    private void animateProgressCircle() {
        double radius = scoreProgressCircle.getRadius();
        double circumference = 2 * Math.PI * radius;
        double percentage = currentScore.getScore();
        double arcLength = circumference * (percentage / 100.0);
        ProgressCircle(circumference);
        createProgressAnimation(circumference, arcLength, percentage);
    }

    private void ProgressCircle(double circumference) {
        scoreProgressCircle.setStrokeWidth(6);
        scoreProgressCircle.setFill(Color.TRANSPARENT);
        scoreProgressCircle.setStrokeLineCap(StrokeLineCap.ROUND);
        scoreProgressCircle.setRotate(-90);
        scoreProgressCircle.setScaleY(-1);

        Color strokeColor = currentScore.getScore() >= 50 ? Color.web("#3DF6A3") : Color.web("#FD3D3D");
        scoreProgressCircle.setStroke(strokeColor);
        scoreProgressCircle.getStrokeDashArray().setAll(circumference);
        scoreProgressCircle.setStrokeDashOffset(circumference);
    }

    private void createProgressAnimation(double circumference, double arcLength, double percentage) {
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(scoreProgressCircle.strokeDashOffsetProperty(),
                circumference - arcLength, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(ANIMATION_DURATION), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        timeline.setOnFinished(e ->
                System.out.println("Score animation complete. Final percentage: " + percentage + "%"));
        timeline.play();
    }

    private void animateTrophyAppearance() {
        Platform.runLater(() -> {
            trophyImage.setScaleX(0);
            trophyImage.setScaleY(0);

            Timeline trophyAnimation = new Timeline(
                    new KeyFrame(Duration.seconds(TROPHY_ANIMATION_DURATION),
                            new KeyValue(trophyImage.scaleXProperty(), 1.0, Interpolator.EASE_OUT),
                            new KeyValue(trophyImage.scaleYProperty(), 1.0, Interpolator.EASE_OUT)
                    )
            );
            trophyAnimation.setDelay(Duration.seconds(TROPHY_ANIMATION_DELAY));
            trophyAnimation.play();
        });
    }

    @FXML
    private void handleRetry() {
        logger.info("Retrying level {}", CURRENT_LEVEL);
        navigateToQuiz();
    }

    @FXML
    private void handleNextLevel() {
        if (!wasScoreSaved) {
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
                MobileViewLoader.load(stage, viewPath);
            } else {
                DesktopViewLoader.load(stage, viewPath);
            }
        } catch (IOException e) {
            logger.error("Failed to load {}: {}", viewName, e.getMessage(), e);
        }
    }
}