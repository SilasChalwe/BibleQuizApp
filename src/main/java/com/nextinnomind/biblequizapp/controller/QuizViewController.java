package com.nextinnomind.biblequizapp.controller;

import com.nextinnomind.biblequizapp.Loader.ViewLoader;
import com.nextinnomind.biblequizapp.Style.QuizViewStyles;
import com.nextinnomind.biblequizapp.model.LevelScore;
import com.nextinnomind.biblequizapp.model.Question;
import com.nextinnomind.biblequizapp.Loader.DataLoader;
import com.nextinnomind.biblequizapp.utils.SoundPlayer;
import com.nextinnomind.biblequizapp.Manager.TimerManager;
import com.nextinnomind.biblequizapp.Manager.ViewModeSelectorManager;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.nextinnomind.biblequizapp.Style.LevelStyle.BACKGROUND_COLOR;
import static com.nextinnomind.biblequizapp.Style.LevelStyle.PLAY_BUTTON_UNLOCKED;
import static com.nextinnomind.biblequizapp.controller.LevelsViewController.CURRENT_LEVEL;

public class QuizViewController {

    private static final Logger logger = LogManager.getLogger(QuizViewController.class);
    private static final double PASS_THRESHOLD = 50.0;

    public Label levelName;
    public StackPane timerCirclePane;
    public Circle timerCircleBg;
    public FlowPane optionsFlowPane;
    //    public HBox optionDBox;
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
    private final AtomicBoolean questionAnswered = new AtomicBoolean(false);
    private static LevelScore levelScore;
    public static LevelScore CURRENT_QUIZ_SCORE;
    private final ViewModeSelectorManager viewModeSelectorManager = new ViewModeSelectorManager();
    private final TimerManager timerManager = new TimerManager();
    @FXML private HBox optionABox;
    @FXML private HBox optionBBox;
    @FXML private HBox optionCBox;
    @FXML private HBox optionDBox;
    @FXML public void initialize() {
        backButton.setOnAction(_ -> handleBack());
        levelName.setText(String.valueOf(CURRENT_LEVEL));

        DataLoader.getInstance().setSelectedQuestions(15, CURRENT_LEVEL);
        selectedQuestions = DataLoader.getInstance().getSelectedQuestions();

        levelScore = new LevelScore();
        levelScore.setLevel(CURRENT_LEVEL);
        levelScore.setTotalQuestions(selectedQuestions.size());
        levelScore.setTimestamp(String.valueOf(System.currentTimeMillis()));
        choiceA.setOnAction(_ -> handleChoice("A"));
        choiceB.setOnAction(_ -> handleChoice("B"));
        choiceC.setOnAction(_ -> handleChoice("C"));
        choiceD.setOnAction(_ -> handleChoice("D"));

        addHoverEffects();
        loadQuestion(currentQuestionIndex);

        logger.info("QuizViewController initialized for level {}", CURRENT_LEVEL);
    }

    private void loadQuestion(int index) {
        timerManager.stopTimer();

        if (index >= selectedQuestions.size()) {
            showScoreView();
            return;
        }

        Question q = selectedQuestions.get(index);
        questionLabel.setText(q.getQuestion());
        choiceB.setStyle(PLAY_BUTTON_UNLOCKED);

        Map<String, String> choices = q.getChoices();
        choiceA.setText("A. " + choices.get("option_a"));
        choiceB.setText("B. " + choices.get("option_b"));
        choiceC.setText("C. " + choices.get("option_c"));
        choiceD.setText("D. " + choices.get("option_d"));


        questionNumberLabel.setText("Question " + (index + 1) + " of " + selectedQuestions.size());

        resetChoiceButtons();
        timerManager.resetTimerVisuals(timerCircleFg, timerLabel);
        questionAnswered.set(false);

        animateOptions();
        timerManager.startTimer(timerCircleFg, timerLabel, this::handleTimeUp);
    }

    private void resetChoiceButtons() {
        for (Button btn : List.of(choiceA, choiceB, choiceC, choiceD)) {
            btn.setStyle(QuizViewStyles.DEFAULT_BUTTON_STYLE);
            btn.setDisable(false);
        }
    }

    private void handleChoice(String selectedLetter) {
        if (questionAnswered.get()) return;
        questionAnswered.set(true);
        timerManager.stopTimer();

        Question current = selectedQuestions.get(currentQuestionIndex);
        String correctOptionKey = current.getCorrectAnswer();
        String correctLetter = correctOptionKey.substring(correctOptionKey.length() - 1).toUpperCase();

        disableAllButtons();
        Button selectedBtn = getButtonByKey(selectedLetter);
        Button correctBtn = getButtonByKey(correctLetter);

        if (selectedLetter.equals(correctLetter)) {
            handleCorrectAnswer(selectedBtn);
        } else {
            handleIncorrectAnswer(selectedBtn, correctBtn);
        }

        new javafx.animation.Timeline(new javafx.animation.KeyFrame(
                javafx.util.Duration.seconds(1.5),
                e -> moveToNextQuestion()
        )).play();
    }

    private void disableAllButtons() {
        choiceA.setDisable(true);
        choiceB.setDisable(true);
        choiceC.setDisable(true);
        choiceD.setDisable(true);
    }

    private void handleCorrectAnswer(Button selectedBtn) {
        score += 100.0 / 15;
        correctCount++;
        levelScore.setScore(score);
        levelScore.setCorrectAnswers(correctCount);
        if (selectedBtn != null) {
            selectedBtn.setStyle(QuizViewStyles.CORRECT_ANSWER_STYLE);
        }
        logger.info("Question {} answered correctly", currentQuestionIndex + 1);
        SoundPlayer.play("/com/nextinnomind/biblequizapp/assets/sounds/correct.wav", 1);
    }

    private void handleIncorrectAnswer(Button selectedBtn, Button correctBtn) {
        score += 0.1;
        if (selectedBtn != null) {
            selectedBtn.setStyle(QuizViewStyles.INCORRECT_ANSWER_STYLE);
        }
        if (correctBtn != null) {
            correctBtn.setStyle(QuizViewStyles.CORRECT_ANSWER_STYLE);
        }
        logger.info("Question {} answered incorrectly.", currentQuestionIndex + 1);
        SoundPlayer.play("/com/nextinnomind/biblequizapp/assets/sounds/level-unlocked.wav", 1);
    }

    private void handleTimeUp() {
        logger.info("Time's up for level {}", CURRENT_LEVEL);
      //  SoundPlayer.play("/audio/timesup.mp3");
        showScoreView();
    }

    private void moveToNextQuestion() {
        currentQuestionIndex++;
        loadQuestion(currentQuestionIndex);
    }

    private void addHoverEffects() {
        for (Button button : new Button[]{choiceA, choiceB, choiceC, choiceD}) {
            button.setOnMouseEntered(_ -> {
                if (!button.isDisabled()) button.setStyle(QuizViewStyles.HOVER_BUTTON_STYLE);
            });
            button.setOnMouseExited(_ -> {
                if (!button.isDisabled()) button.setStyle(QuizViewStyles.DEFAULT_BUTTON_STYLE);
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
        timerManager.stopTimer();
        levelScore.setScore(score);
        levelScore.setCorrectAnswers(correctCount);
        levelScore.setTimestamp(Instant.now().toString());
        CURRENT_QUIZ_SCORE = levelScore;

        double percentage = (score / 100.0) * 100;
        if (percentage >= PASS_THRESHOLD) {
            DataLoader.getInstance().saveScore(levelScore);
            logger.info("Score saved for level {}", CURRENT_LEVEL);
        }

        loadView("/com/nextinnomind/biblequizapp/views/score-view.fxml");
    }

    @FXML
    private void handleBack() {
        timerManager.stopTimer();
        loadView("/com/nextinnomind/biblequizapp/views/levels-view.fxml");
    }

    private void loadView(String fxmlPath) {
        Stage window = (Stage) backButton.getScene().getWindow();
        String viewMode = ViewModeSelectorManager.getViewMode();
        ViewLoader.loadMainView(window,viewMode,fxmlPath);

    }
    public void animateOptions() {
        List<HBox> optionBoxes = List.of(optionABox, optionBBox, optionCBox, optionDBox);

        double delay = 0;
        for (HBox box : optionBoxes) {
            box.setOpacity(0);

            FadeTransition ft = new FadeTransition(Duration.millis(300), box);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.setDelay(Duration.millis(delay));
            ft.play();

            delay += 200;
        }
    }

}
