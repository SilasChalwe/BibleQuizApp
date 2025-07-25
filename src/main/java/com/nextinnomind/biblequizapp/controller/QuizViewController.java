////package com.nextinnomind.biblequizapp.controller;
////
//////import com.nextinnomind.biblequizapp.model.LevelScore;
////import com.nextinnomind.biblequizapp.model.LevelScore;
////import com.nextinnomind.biblequizapp.model.OpenedLevel;
////import com.nextinnomind.biblequizapp.model.Question;
////import com.nextinnomind.biblequizapp.utils.DesktopViewLoader;
////import com.nextinnomind.biblequizapp.utils.JsonDataLoader;
////import com.nextinnomind.biblequizapp.utils.MobileViewLoader;
////import com.nextinnomind.biblequizapp.utils.ViewModeSelector;
////import javafx.animation.KeyFrame;
////import javafx.animation.KeyValue;
////import javafx.animation.Timeline;
////import javafx.application.Platform;
////import javafx.fxml.FXML;
////import javafx.scene.control.Button;
////import javafx.scene.control.Label;
////import javafx.scene.layout.StackPane;
////import javafx.scene.paint.Color;
////import javafx.scene.shape.Circle;
////import javafx.stage.Stage;
////import javafx.util.Duration;
////import org.apache.logging.log4j.LogManager;
////import org.apache.logging.log4j.Logger;
////
////import java.io.IOException;
////import java.time.Instant;
////import java.util.ArrayList;
////import java.util.List;
////import java.util.Map;
////import java.util.concurrent.atomic.AtomicBoolean;
////
////import static com.nextinnomind.biblequizapp.controller.LevelsViewController.CURRENT_LEVEL;
////
////public class QuizViewController {
////
////    private static final Logger logger = LogManager.getLogger(QuizViewController.class);
////    private static final int TIMER_DURATION = 30; // seconds
////
////    public Label levelName;
////    public StackPane timerCirclePane;
////    public Circle timerCircleBg;
////    @FXML private Button backButton;
////    @FXML private Label questionLabel;
////    @FXML private Label questionNumberLabel;
////    @FXML private Label timerLabel;
////    @FXML private Button choiceA, choiceB, choiceC, choiceD;
////    @FXML private Circle timerCircleFg;
////
////    private List<Question> selectedQuestions = new ArrayList<>();
////    private int currentQuestionIndex = 0;
////
////    private double score = 0;
////    private int correctCount = 0;
////
////    private Thread timerThread;
////    private Timeline timerTimeline;
////    private final AtomicBoolean questionAnsweredAtomic = new AtomicBoolean(false);
////
////    private static LevelScore levelScore;
////    //public static LevelScore NEW_UNSAVED = levelScore;
////    private final ViewModeSelector viewModeSelector = new ViewModeSelector();
////
////    public QuizViewController() {
////
////    }
////
////    @FXML
////    public void initialize() {
////        backButton.setOnAction(e -> handleBack());
////        levelName.setText(String.valueOf(CURRENT_LEVEL));
////
////        JsonDataLoader.getInstance().setSelectedQuestions(15, CURRENT_LEVEL);
////        selectedQuestions = JsonDataLoader.getInstance().getSelectedQuestions();
////
////        levelScore = new LevelScore();
////        levelScore.setLevel(CURRENT_LEVEL);
////        levelScore.setTotalQuestions(selectedQuestions.size());
////        levelScore.setTimestamp(String.valueOf(System.currentTimeMillis()));
////
////        choiceA.setOnAction(e -> handleChoice("A"));
////        choiceB.setOnAction(e -> handleChoice("B"));
////        choiceC.setOnAction(e -> handleChoice("C"));
////        choiceD.setOnAction(e -> handleChoice("D"));
////
////        // Add hover effects to choice buttons
////        addHoverEffects();
////
////        // Load first question
////        loadQuestion(currentQuestionIndex);
////
////        logger.info("QuizViewController initialized for level {}", CURRENT_LEVEL);
////    }
////
////    private void loadQuestion(int index) {
////        // Stop any existing timer first
////        stopTimer();
////
////        if (index >= selectedQuestions.size()) {
////            showScoreView();
////            return;
////        }
////
////        Question q = selectedQuestions.get(index);
////        questionLabel.setText(q.getQuestion());
////        // questionLabel.setText("jdwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwhuyfgfbnxcvgvcghgfgcg          hggggjhcvmcvc hgcvmscghvc bcvvhgscvmmmmmmmm gcvgvhvdmvcc cccccccccccccccccccchfdgfcnvsdfkffffffdssssxxxxvncccccccccccccccccccccccccccccccc gd xgcfgx          ggggggggggggggghssssssssssssssssssssssssss");
////
////        Map<String, String> choices = q.getChoices();
////        choiceA.setText("A. " + choices.get("option_a"));
////        choiceB.setText("B. " + choices.get("option_b"));
////        choiceC.setText("C. " + choices.get("option_c"));
////        choiceD.setText("D. " + choices.get("option_d"));
////
////        questionNumberLabel.setText("Question " + (index + 1) + " of " + selectedQuestions.size());
////
////        // Reset everything for new question
////        resetChoiceButtons();
////        resetTimerVisuals();
////        questionAnsweredAtomic.set(false);
////
////        // Start timer for new question
////        startTimer();
////
////        logger.info("Loaded question {}: {}", index + 1, q.getQuestion());
////    }
////
////    private void resetChoiceButtons() {
////        String defaultStyle = "-fx-background-color: white; -fx-text-fill: #2C3E50; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2); -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 12px; -fx-border-width: 1; -fx-pref-width: 340; -fx-alignment: CENTER_LEFT;";
////
////        for (Button btn : List.of(choiceA, choiceB, choiceC, choiceD)) {
////            btn.setStyle(defaultStyle);
////            btn.setDisable(false);
////        }
////    }
////
////    private void handleChoice(String selectedLetter) {
////        if (questionAnsweredAtomic.get()) return;
////        questionAnsweredAtomic.set(true);
////        stopTimer();
////
////        Question current = selectedQuestions.get(currentQuestionIndex);
////        String correctOptionKey = current.getCorrectAnswer();
////        String correctLetter = correctOptionKey.substring(correctOptionKey.length() - 1).toUpperCase();
////
////        // Disable all buttons
////        choiceA.setDisable(true);
////        choiceB.setDisable(true);
////        choiceC.setDisable(true);
////        choiceD.setDisable(true);
////
////        Button selectedBtn = getButtonByKey(selectedLetter);
////        Button correctBtn = getButtonByKey(correctLetter);
////
////        if (selectedLetter.equals(correctLetter)) {
////            score += 100.0 / 15;
////            correctCount++;
////            levelScore.setScore(score);
////            levelScore.setCorrectAnswers(correctCount);
////            if (selectedBtn != null)
////                selectedBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20;");
////            logger.info("Question {} answered correctly with choice {}", currentQuestionIndex + 1, selectedLetter);
////        } else {
////            score += 0.1;
////            if (selectedBtn != null)
////                selectedBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20;");
////            if (correctBtn != null)
////                correctBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20;");
////            logger.info("Question {} answered incorrectly with choice {}. Correct was {}", currentQuestionIndex + 1, selectedLetter, correctLetter);
////        }
////
////        // Move to next question after delay
////        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> moveToNextQuestion()));
////        delay.play();
////    }
////
////    /**
////     * Starts a synchronized timer with both visual circle animation and countdown
////     */
////    private void startTimer() {
////        // Stop any existing timer first
////        stopTimer();
////
////        // Reset timer state
////        questionAnsweredAtomic.set(false);
////
////        // Calculate circle properties
////        double radius = timerCircleFg.getRadius(); // 25 from FXML
////        double circleLength = 2 * Math.PI * radius; // approximately 157.08
////
////        // Reset circle to initial state
////        timerCircleFg.setStrokeDashOffset(0);
////
////        // Create the visual timeline for circle animation
////        timerTimeline = new Timeline();
////        KeyValue kv = new KeyValue(timerCircleFg.strokeDashOffsetProperty(), circleLength);
////        KeyFrame kf = new KeyFrame(Duration.seconds(TIMER_DURATION), kv);
////        timerTimeline.getKeyFrames().add(kf);
////
////        // Handle timeline completion
////        timerTimeline.setOnFinished(e -> {
////            if (!questionAnsweredAtomic.get()) {
////                handleTimeUp();
////            }
////        });
////
////        // Start the countdown thread
////        timerThread = new Thread(() -> {
////            try {
////                for (int i = TIMER_DURATION; i >= 0; i--) {
////                    if (Thread.currentThread().isInterrupted() || questionAnsweredAtomic.get()) {
////                        return;
////                    }
////
////                    final int timeLeft = i;
////                    Platform.runLater(() -> {
////                        timerLabel.setText(String.valueOf(timeLeft));
////
////                        // Change color based on remaining time
////                        if (timeLeft <= 5) {
////                            timerCircleFg.setStroke(Color.web("#FF4757")); // Critical red
////                        } else if (timeLeft <= 10) {
////                            timerCircleFg.setStroke(Color.web("#FFA726")); // Warning orange
////                        } else {
////                            timerCircleFg.setStroke(Color.web("#FF6B6B")); // Default red
////                        }
////                    });
////
////                    Thread.sleep(1000);
////                }
////
////                // Time's up
////                if (!questionAnsweredAtomic.get()) {
////                    Platform.runLater(this::handleTimeUp);
////                }
////
////            } catch (InterruptedException e) {
////                logger.info("Timer thread interrupted");
////                // Thread was interrupted, which is expected when stopping timer
////            }
////        });
////
////        timerThread.setDaemon(true);
////        timerThread.start();
////
////        // Start the visual animation
////        timerTimeline.play();
////
////        logger.info("Timer started for question {}", currentQuestionIndex + 1);
////    }
////
////    /**
////     * Stops the timer completely and resets the visual state
////     */
////    private void stopTimer() {
////        // Stop the timeline animation
////        if (timerTimeline != null) {
////            timerTimeline.stop();
////            timerTimeline = null;
////        }
////
////        // Stop the countdown thread
////        if (timerThread != null && timerThread.isAlive()) {
////            timerThread.interrupt();
////            try {
////                timerThread.join(100); // Wait up to 100ms for thread to finish
////            } catch (InterruptedException e) {
////                Thread.currentThread().interrupt();
////            }
////            timerThread = null;
////        }
////
////        logger.info("Timer stopped");
////    }
////
////    /**
////     * Resets the timer visual state for a new question
////     */
////    private void resetTimerVisuals() {
////        Platform.runLater(() -> {
////            timerLabel.setText(String.valueOf(TIMER_DURATION));
////            timerCircleFg.setStrokeDashOffset(0);
////            timerCircleFg.setStroke(Color.web("#FF6B6B")); // Reset to default color
////        });
////    }
////
////    /**
////     * Handles what happens when time runs out
////     */
////    private void handleTimeUp() {
////        logger.info(levelScore.toString());
////        showScoreView();
////    }
////
////    /**
////     * Moves to the next question with proper cleanup
////     */
////    private void moveToNextQuestion() {
////        currentQuestionIndex++;
////        loadQuestion(currentQuestionIndex);
////    }
////
////    /**
////     * AMaterial Design hover effects to choice buttons
////     */
////    private void addHoverEffects() {
////        String defaultStyle = "-fx-background-color: white; -fx-text-fill: #2C3E50; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2); -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 12px; -fx-border-width: 1; -fx-pref-width: 340; -fx-alignment: CENTER_LEFT;";
////        String hoverStyle = "-fx-background-color: #f8f9fa; -fx-text-fill: #2C3E50; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 12, 0, 0, 4); -fx-border-color: #667eea; -fx-border-radius: 12px; -fx-border-width: 2; -fx-pref-width: 340; -fx-alignment: CENTER_LEFT;";
////
////        Button[] buttons = {choiceA, choiceB, choiceC, choiceD};
////
////        for (Button button : buttons) {
////            button.setOnMouseEntered(e -> {
////                if (!button.isDisabled()) {
////                    button.setStyle(hoverStyle);
////                }
////            });
////            button.setOnMouseExited(e -> {
////                if (!button.isDisabled()) {
////                    button.setStyle(defaultStyle);
////                }
////            });
////        }
////    }
////
////    private Button getButtonByKey(String key) {
////        return switch (key) {
////            case "A" -> choiceA;
////            case "B" -> choiceB;
////            case "C" -> choiceC;
////            case "D" -> choiceD;
////            default -> null;
////        };
////    }
////
////    private void showScoreView() {
////        stopTimer();
////        levelScore.setScore(score);
////        levelScore.setCorrectAnswers(correctCount);
////        levelScore.setTimestamp(Instant.now().toString());
////        //if(levelScore.getScore()>50){
////        JsonDataLoader.getInstance().saveScore(levelScore);
////        //  }
////
////        try {
////            Stage stage = (Stage) backButton.getScene().getWindow();
////
////            String scoreViewPath = "/com/nextinnomind/biblequizapp/views/score-view.fxml";
////            String viewMode = viewModeSelector.getViewMode();
////
////            if ("mobile".equalsIgnoreCase(viewMode)) {
////                MobileViewLoader.load(stage, scoreViewPath);
////            } else {
////                DesktopViewLoader.load(stage, scoreViewPath);
////            }
////
////            logger.info("Showing score view for level {}", levelScore.getLevel());
////        } catch (IOException e) {
////            logger.error("Failed to load score view: {}", e.getMessage(), e);
////        }
////    }
////
////    @FXML
////    private void handleBack() {
////        stopTimer();
////        try {
////            Stage stage = (Stage) backButton.getScene().getWindow();
////
////            String levelsViewPath = "/com/nextinnomind/biblequizapp/views/levels-view.fxml";
////            String viewMode = viewModeSelector.getViewMode();
////
////            if ("mobile".equalsIgnoreCase(viewMode)) {
////                MobileViewLoader.load(stage, levelsViewPath);
////            } else {
////                DesktopViewLoader.load(stage, levelsViewPath);
////            }
////
////            logger.info("Navigated back to levels view");
////        } catch (IOException e) {
////            logger.error("Failed to load levels view: {}", e.getMessage(), e);
////        }
////    }
////}
////
////
////
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//package com.nextinnomind.biblequizapp.controller;
//
//import com.nextinnomind.biblequizapp.model.LevelScore;
//import com.nextinnomind.biblequizapp.model.Question;
//import com.nextinnomind.biblequizapp.utils.DesktopViewLoader;
//import com.nextinnomind.biblequizapp.utils.JsonDataLoader;
//import com.nextinnomind.biblequizapp.utils.MobileViewLoader;
//import com.nextinnomind.biblequizapp.utils.SoundPlayer;
//import com.nextinnomind.biblequizapp.utils.ViewModeSelector;
//import javafx.animation.KeyFrame;
//import javafx.animation.KeyValue;
//import javafx.animation.Timeline;
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.io.IOException;
//import java.time.Instant;
//import java.util.*;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import static com.nextinnomind.biblequizapp.controller.LevelsViewController.CURRENT_LEVEL;
//
//public class QuizViewController {
//
//    private static final Logger logger = LogManager.getLogger(QuizViewController.class);
//    private static final int TIMER_DURATION = 30;
//
//    public Label levelName;
//    public StackPane timerCirclePane;
//    public Circle timerCircleBg;
//    @FXML private Button backButton;
//    @FXML private Label questionLabel;
//    @FXML private Label questionNumberLabel;
//    @FXML private Label timerLabel;
//    @FXML private Button choiceA, choiceB, choiceC, choiceD;
//    @FXML private Circle timerCircleFg;
//
//    private List<Question> selectedQuestions = new ArrayList<>();
//    private int currentQuestionIndex = 0;
//    private double score = 0;
//    private int correctCount = 0;
//
//    private Thread timerThread;
//    private Timeline timerTimeline;
//    private final AtomicBoolean questionAnsweredAtomic = new AtomicBoolean(false);
//
//    private static LevelScore levelScore;
//    private final ViewModeSelector viewModeSelector = new ViewModeSelector();
//
//    public QuizViewController() {}
//
//    @FXML
//    public void initialize() {
//        backButton.setOnAction(e -> handleBack());
//        levelName.setText(String.valueOf(CURRENT_LEVEL));
//
//        JsonDataLoader.getInstance().setSelectedQuestions(15, CURRENT_LEVEL);
//        selectedQuestions = JsonDataLoader.getInstance().getSelectedQuestions();
//
//        levelScore = new LevelScore();
//        levelScore.setLevel(CURRENT_LEVEL);
//        levelScore.setTotalQuestions(selectedQuestions.size());
//        levelScore.setTimestamp(String.valueOf(System.currentTimeMillis()));
//
//        choiceA.setOnAction(e -> handleChoice("A"));
//        choiceB.setOnAction(e -> handleChoice("B"));
//        choiceC.setOnAction(e -> handleChoice("C"));
//        choiceD.setOnAction(e -> handleChoice("D"));
//
//        addHoverEffects();
//        loadQuestion(currentQuestionIndex);
//
//        logger.info("QuizViewController initialized for level {}", CURRENT_LEVEL);
//    }
//
//    private void loadQuestion(int index) {
//        stopTimer();
//
//        if (index >= selectedQuestions.size()) {
//            showScoreView();
//            return;
//        }
//
//        Question q = selectedQuestions.get(index);
//        questionLabel.setText(q.getQuestion());
//
//        Map<String, String> choices = q.getChoices();
//        choiceA.setText("A. " + choices.get("option_a"));
//        choiceB.setText("B. " + choices.get("option_b"));
//        choiceC.setText("C. " + choices.get("option_c"));
//        choiceD.setText("D. " + choices.get("option_d"));
//
//        questionNumberLabel.setText("Question " + (index + 1) + " of " + selectedQuestions.size());
//
//        resetChoiceButtons();
//        resetTimerVisuals();
//        questionAnsweredAtomic.set(false);
//
//        startTimer();
//
//        logger.info("Loaded question {}: {}", index + 1, q.getQuestion());
//    }
//
//    private void resetChoiceButtons() {
//        String defaultStyle = "-fx-background-color: white; -fx-text-fill: #2C3E50; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2); -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 12px; -fx-border-width: 1; -fx-pref-width: 340; -fx-alignment: CENTER_LEFT;";
//        for (Button btn : List.of(choiceA, choiceB, choiceC, choiceD)) {
//            btn.setStyle(defaultStyle);
//            btn.setDisable(false);
//        }
//    }
//
//    private void handleChoice(String selectedLetter) {
//        if (questionAnsweredAtomic.get()) return;
//        questionAnsweredAtomic.set(true);
//        stopTimer();
//
//        Question current = selectedQuestions.get(currentQuestionIndex);
//        String correctOptionKey = current.getCorrectAnswer();
//        String correctLetter = correctOptionKey.substring(correctOptionKey.length() - 1).toUpperCase();
//
//        choiceA.setDisable(true);
//        choiceB.setDisable(true);
//        choiceC.setDisable(true);
//        choiceD.setDisable(true);
//
//        Button selectedBtn = getButtonByKey(selectedLetter);
//        Button correctBtn = getButtonByKey(correctLetter);
//
//        if (selectedLetter.equals(correctLetter)) {
//            score += 100.0 / 15;
//            correctCount++;
//            levelScore.setScore(score);
//            levelScore.setCorrectAnswers(correctCount);
//            if (selectedBtn != null)
//                selectedBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20;");
//            logger.info("Question {} answered correctly with choice {}", currentQuestionIndex + 1, selectedLetter);
//
//            // ✅ Play correct answer sound
//            SoundPlayer.play("/com/nextinnomind/biblequizapp/assets/sounds/correct.wav", 1);
//
//        } else {
//            score += 0.1;
//            if (selectedBtn != null)
//                selectedBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20;");
//            if (correctBtn != null)
//                correctBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20;");
//            logger.info("Question {} answered incorrectly with choice {}. Correct was {}", currentQuestionIndex + 1, selectedLetter, correctLetter);
//
//            //  Play incorrect answer sound
//            SoundPlayer.play("/com/nextinnomind/biblequizapp/assets/sounds/level-unlocked.wav",1);
//        }
//
//        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> moveToNextQuestion()));
//        delay.play();
//    }
//
//    private void startTimer() {
//        stopTimer();
//        questionAnsweredAtomic.set(false);
//
//        double radius = timerCircleFg.getRadius();
//        double circleLength = 2 * Math.PI * radius;
//        timerCircleFg.setStrokeDashOffset(0);
//
//        timerTimeline = new Timeline();
//        KeyValue kv = new KeyValue(timerCircleFg.strokeDashOffsetProperty(), circleLength);
//        KeyFrame kf = new KeyFrame(Duration.seconds(TIMER_DURATION), kv);
//        timerTimeline.getKeyFrames().add(kf);
//
//        timerTimeline.setOnFinished(e -> {
//            if (!questionAnsweredAtomic.get()) handleTimeUp();
//        });
//
//        timerThread = new Thread(() -> {
//            try {
//                for (int i = TIMER_DURATION; i >= 0; i--) {
//                    if (Thread.currentThread().isInterrupted() || questionAnsweredAtomic.get()) return;
//
//                    final int timeLeft = i;
//                    Platform.runLater(() -> {
//                        timerLabel.setText(String.valueOf(timeLeft));
//                        timerCircleFg.setStroke(
//                                timeLeft <= 5 ? Color.web("#FF4757") :
//                                        timeLeft <= 10 ? Color.web("#FFA726") :
//                                                Color.web("#FF6B6B"));
//                    });
//
//                    Thread.sleep(1000);
//                }
//
//                if (!questionAnsweredAtomic.get()) {
//                    Platform.runLater(this::handleTimeUp);
//                }
//
//            } catch (InterruptedException ignored) {
//                logger.info("Timer thread interrupted");
//            }
//        });
//
//        timerThread.setDaemon(true);
//        timerThread.start();
//        timerTimeline.play();
//        logger.info("Timer started for question {}", currentQuestionIndex + 1);
//    }
//
//    private void stopTimer() {
//        if (timerTimeline != null) {
//            timerTimeline.stop();
//            timerTimeline = null;
//        }
//
//        if (timerThread != null && timerThread.isAlive()) {
//            timerThread.interrupt();
//            try {
//                timerThread.join(100);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//            timerThread = null;
//        }
//
//        logger.info("Timer stopped");
//    }
//
//    private void resetTimerVisuals() {
//        Platform.runLater(() -> {
//            timerLabel.setText(String.valueOf(TIMER_DURATION));
//            timerCircleFg.setStrokeDashOffset(0);
//            timerCircleFg.setStroke(Color.web("#FF6B6B"));
//        });
//    }
//
//    private void handleTimeUp() {
//        logger.info(levelScore.toString());
//
//        // ⏰ Play timeout sound
//        SoundPlayer.play("/audio/timesup.mp3");
//
//        showScoreView();
//    }
//
//    private void moveToNextQuestion() {
//        currentQuestionIndex++;
//        loadQuestion(currentQuestionIndex);
//    }
//
//    private void addHoverEffects() {
//        String defaultStyle = "-fx-background-color: white; -fx-text-fill: #2C3E50; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2); -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 12px; -fx-border-width: 1; -fx-pref-width: 340; -fx-alignment: CENTER_LEFT;";
//        String hoverStyle = "-fx-background-color: #f8f9fa; -fx-text-fill: #2C3E50; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 12, 0, 0, 4); -fx-border-color: #667eea; -fx-border-radius: 12px; -fx-border-width: 2; -fx-pref-width: 340; -fx-alignment: CENTER_LEFT;";
//
//        for (Button button : new Button[]{choiceA, choiceB, choiceC, choiceD}) {
//            button.setOnMouseEntered(e -> {
//                if (!button.isDisabled()) button.setStyle(hoverStyle);
//            });
//            button.setOnMouseExited(e -> {
//                if (!button.isDisabled()) button.setStyle(defaultStyle);
//            });
//        }
//    }
//
//    private Button getButtonByKey(String key) {
//        return switch (key) {
//            case "A" -> choiceA;
//            case "B" -> choiceB;
//            case "C" -> choiceC;
//            case "D" -> choiceD;
//            default -> null;
//        };
//    }
//
//    private void showScoreView() {
//        stopTimer();
//        levelScore.setScore(score);
//        levelScore.setCorrectAnswers(correctCount);
//        levelScore.setTimestamp(Instant.now().toString());
//
//        JsonDataLoader.getInstance().saveScore(levelScore);
//
//        try {
//            Stage stage = (Stage) backButton.getScene().getWindow();
//            String path = "/com/nextinnomind/biblequizapp/views/score-view.fxml";
//            String viewMode = viewModeSelector.getViewMode();
//
//            if ("mobile".equalsIgnoreCase(viewMode)) {
//                MobileViewLoader.load(stage, path);
//            } else {
//                DesktopViewLoader.load(stage, path);
//            }
//
//            logger.info("Showing score view for level {}", levelScore.getLevel());
//        } catch (IOException e) {
//            logger.error("Failed to load score view: {}", e.getMessage(), e);
//        }
//    }
//
//    @FXML
//    private void handleBack() {
//        stopTimer();
//        try {
//            Stage stage = (Stage) backButton.getScene().getWindow();
//            String path = "/com/nextinnomind/biblequizapp/views/levels-view.fxml";
//            String viewMode = viewModeSelector.getViewMode();
//
//            if ("mobile".equalsIgnoreCase(viewMode)) {
//                MobileViewLoader.load(stage, path);
//            } else {
//                DesktopViewLoader.load(stage, path);
//            }
//
//            logger.info("Navigated back to levels view");
//        } catch (IOException e) {
//            logger.error("Failed to load levels view: {}", e.getMessage(), e);
//        }
//    }
//}


package com.nextinnomind.biblequizapp.controller;

import com.nextinnomind.biblequizapp.model.LevelScore;
import com.nextinnomind.biblequizapp.model.Question;
import com.nextinnomind.biblequizapp.utils.DesktopViewLoader;
import com.nextinnomind.biblequizapp.utils.JsonDataLoader;
import com.nextinnomind.biblequizapp.utils.MobileViewLoader;
import com.nextinnomind.biblequizapp.utils.SoundPlayer;
import com.nextinnomind.biblequizapp.utils.ViewModeSelector;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.nextinnomind.biblequizapp.controller.LevelsViewController.CURRENT_LEVEL;

public class QuizViewController {

    private static final Logger logger = LogManager.getLogger(QuizViewController.class);
    private static final int TIMER_DURATION = 30;
    private static final double PASS_THRESHOLD = 50.0; // 50% threshold to pass and unlock next level

    public Label levelName;
    public StackPane timerCirclePane;
    public Circle timerCircleBg;
    @FXML private Button backButton;
    @FXML private Label questionLabel;
    @FXML private Label questionNumberLabel;
    @FXML private Label timerLabel;
    @FXML private Button choiceA, choiceB, choiceC, choiceD;
    @FXML private Circle timerCircleFg;

    private List<Question> selectedQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private double score = 0;
    private int correctCount = 0;

    private Thread timerThread;
    private Timeline timerTimeline;
    private final AtomicBoolean questionAnsweredAtomic = new AtomicBoolean(false);

    private static LevelScore levelScore;
    // Static variable to pass score data to ScoreViewController even if not saved
    public static LevelScore CURRENT_QUIZ_SCORE;

    private final ViewModeSelector viewModeSelector = new ViewModeSelector();

    public QuizViewController() {}

    @FXML
    public void initialize() {
        backButton.setOnAction(e -> handleBack());
        levelName.setText(String.valueOf(CURRENT_LEVEL));

        JsonDataLoader.getInstance().setSelectedQuestions(15, CURRENT_LEVEL);
        selectedQuestions = JsonDataLoader.getInstance().getSelectedQuestions();

        levelScore = new LevelScore();
        levelScore.setLevel(CURRENT_LEVEL);
        levelScore.setTotalQuestions(selectedQuestions.size());
        levelScore.setTimestamp(String.valueOf(System.currentTimeMillis()));

        choiceA.setOnAction(e -> handleChoice("A"));
        choiceB.setOnAction(e -> handleChoice("B"));
        choiceC.setOnAction(e -> handleChoice("C"));
        choiceD.setOnAction(e -> handleChoice("D"));

        addHoverEffects();
        loadQuestion(currentQuestionIndex);

        logger.info("QuizViewController initialized for level {}", CURRENT_LEVEL);
    }

    private void loadQuestion(int index) {
        stopTimer();

        if (index >= selectedQuestions.size()) {
            showScoreView();
            return;
        }

        Question q = selectedQuestions.get(index);
        questionLabel.setText(q.getQuestion());

        Map<String, String> choices = q.getChoices();
        choiceA.setText("A. " + choices.get("option_a"));
        choiceB.setText("B. " + choices.get("option_b"));
        choiceC.setText("C. " + choices.get("option_c"));
        choiceD.setText("D. " + choices.get("option_d"));

        questionNumberLabel.setText("Question " + (index + 1) + " of " + selectedQuestions.size());

        resetChoiceButtons();
        resetTimerVisuals();
        questionAnsweredAtomic.set(false);

        startTimer();

        logger.info("Loaded question {}: {}", index + 1, q.getQuestion());
    }

    private void resetChoiceButtons() {
        String defaultStyle = "-fx-background-color: white; -fx-text-fill: #2C3E50; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2); -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 12px; -fx-border-width: 1; -fx-pref-width: 340; -fx-alignment: CENTER_LEFT;";
        for (Button btn : List.of(choiceA, choiceB, choiceC, choiceD)) {
            btn.setStyle(defaultStyle);
            btn.setDisable(false);
        }
    }

    private void handleChoice(String selectedLetter) {
        if (questionAnsweredAtomic.get()) return;
        questionAnsweredAtomic.set(true);
        stopTimer();

        Question current = selectedQuestions.get(currentQuestionIndex);
        String correctOptionKey = current.getCorrectAnswer();
        String correctLetter = correctOptionKey.substring(correctOptionKey.length() - 1).toUpperCase();

        choiceA.setDisable(true);
        choiceB.setDisable(true);
        choiceC.setDisable(true);
        choiceD.setDisable(true);

        Button selectedBtn = getButtonByKey(selectedLetter);
        Button correctBtn = getButtonByKey(correctLetter);

        if (selectedLetter.equals(correctLetter)) {
            score += 100.0 / 15;
            correctCount++;
            levelScore.setScore(score);
            levelScore.setCorrectAnswers(correctCount);
            if (selectedBtn != null)
                selectedBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20;");
            logger.info("Question {} answered correctly with choice {}", currentQuestionIndex + 1, selectedLetter);

            // Play correct answer sound
            SoundPlayer.play("/com/nextinnomind/biblequizapp/assets/sounds/correct.wav", 1);

        } else {
            score += 0.1;
            if (selectedBtn != null)
                selectedBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20;");
            if (correctBtn != null)
                correctBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20;");
            logger.info("Question {} answered incorrectly with choice {}. Correct was {}", currentQuestionIndex + 1, selectedLetter, correctLetter);

            // Play incorrect answer sound
            SoundPlayer.play("/com/nextinnomind/biblequizapp/assets/sounds/level-unlocked.wav",1);
        }

        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> moveToNextQuestion()));
        delay.play();
    }

    private void startTimer() {
        stopTimer();
        questionAnsweredAtomic.set(false);

        double radius = timerCircleFg.getRadius();
        double circleLength = 2 * Math.PI * radius;
        timerCircleFg.setStrokeDashOffset(0);

        timerTimeline = new Timeline();
        KeyValue kv = new KeyValue(timerCircleFg.strokeDashOffsetProperty(), circleLength);
        KeyFrame kf = new KeyFrame(Duration.seconds(TIMER_DURATION), kv);
        timerTimeline.getKeyFrames().add(kf);

        timerTimeline.setOnFinished(e -> {
            if (!questionAnsweredAtomic.get()) handleTimeUp();
        });

        timerThread = new Thread(() -> {
            try {
                for (int i = TIMER_DURATION; i >= 0; i--) {
                    if (Thread.currentThread().isInterrupted() || questionAnsweredAtomic.get()) return;

                    final int timeLeft = i;
                    Platform.runLater(() -> {
                        timerLabel.setText(String.valueOf(timeLeft));
                        timerCircleFg.setStroke(
                                timeLeft <= 5 ? Color.web("#FF4757") :
                                        timeLeft <= 10 ? Color.web("#FFA726") :
                                                Color.web("#FF6B6B"));
                    });

                    Thread.sleep(1000);
                }

                if (!questionAnsweredAtomic.get()) {
                    Platform.runLater(this::handleTimeUp);
                }

            } catch (InterruptedException ignored) {
                logger.info("Timer thread interrupted");
            }
        });

        timerThread.setDaemon(true);
        timerThread.start();
        timerTimeline.play();
        logger.info("Timer started for question {}", currentQuestionIndex + 1);
    }

    private void stopTimer() {
        if (timerTimeline != null) {
            timerTimeline.stop();
            timerTimeline = null;
        }

        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
            try {
                timerThread.join(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            timerThread = null;
        }

        logger.info("Timer stopped");
    }

    private void resetTimerVisuals() {
        Platform.runLater(() -> {
            timerLabel.setText(String.valueOf(TIMER_DURATION));
            timerCircleFg.setStrokeDashOffset(0);
            timerCircleFg.setStroke(Color.web("#FF6B6B"));
        });
    }

    private void handleTimeUp() {
        logger.info("Time's up for level {}", CURRENT_LEVEL);

        // Play timeout sound
        SoundPlayer.play("/audio/timesup.mp3");

        showScoreView();
    }

    private void moveToNextQuestion() {
        currentQuestionIndex++;
        loadQuestion(currentQuestionIndex);
    }

    private void addHoverEffects() {
        String defaultStyle = "-fx-background-color: white; -fx-text-fill: #2C3E50; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2); -fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 12px; -fx-border-width: 1; -fx-pref-width: 340; -fx-alignment: CENTER_LEFT;";
        String hoverStyle = "-fx-background-color: #f8f9fa; -fx-text-fill: #2C3E50; -fx-font-size: 16px; -fx-font-weight: 500; -fx-background-radius: 12px; -fx-padding: 15 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 12, 0, 0, 4); -fx-border-color: #667eea; -fx-border-radius: 12px; -fx-border-width: 2; -fx-pref-width: 340; -fx-alignment: CENTER_LEFT;";

        for (Button button : new Button[]{choiceA, choiceB, choiceC, choiceD}) {
            button.setOnMouseEntered(e -> {
                if (!button.isDisabled()) button.setStyle(hoverStyle);
            });
            button.setOnMouseExited(e -> {
                if (!button.isDisabled()) button.setStyle(defaultStyle);
            });
        }
    }

    private Button getButtonByKey(String key) {
        return switch (key) {
            case "A" -> choiceA;
            case "B" -> choiceB;
            case "C" -> choiceC;
            case "D" -> choiceD;
            default -> null;
        };
    }

    private void showScoreView() {
        stopTimer();
        levelScore.setScore(score);
        levelScore.setCorrectAnswers(correctCount);
        levelScore.setTimestamp(Instant.now().toString());

        // Calculate percentage to determine if user passed
        double percentage = (score / 100.0) * 100;
        boolean passed = percentage >= PASS_THRESHOLD;

        // Always set the current quiz score for the ScoreViewController to access
        CURRENT_QUIZ_SCORE = levelScore;

        // Only save to file if score is 50% or above (to unlock next level)
        if (passed) {
            JsonDataLoader.getInstance().saveScore(levelScore);
            logger.info("Score {}% saved for level {} - Next level unlocked", String.format("%.1f", percentage), CURRENT_LEVEL);
        } else {
            logger.info("Score {}% below threshold (50%) for level {} - Score not saved, next level remains locked", String.format("%.1f", percentage), CURRENT_LEVEL);
        }

        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            String path = "/com/nextinnomind/biblequizapp/views/score-view.fxml";
            String viewMode = viewModeSelector.getViewMode();

            if ("mobile".equalsIgnoreCase(viewMode)) {
                MobileViewLoader.load(stage, path);
            } else {
                DesktopViewLoader.load(stage, path);
            }

            logger.info("Showing score view for level {}", levelScore.getLevel());
        } catch (IOException e) {
            logger.error("Failed to load score view: {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleBack() {
        stopTimer();
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            String path = "/com/nextinnomind/biblequizapp/views/levels-view.fxml";
            String viewMode = viewModeSelector.getViewMode();

            if ("mobile".equalsIgnoreCase(viewMode)) {
                MobileViewLoader.load(stage, path);
            } else {
                DesktopViewLoader.load(stage, path);
            }

            logger.info("Navigated back to levels view");
        } catch (IOException e) {
            logger.error("Failed to load levels view: {}", e.getMessage(), e);
        }
    }
}
