package com.nextinnomind.biblequizapp.controller;


import com.nextinnomind.biblequizapp.model.LevelScore;
import com.nextinnomind.biblequizapp.model.OpenedLevel;
import com.nextinnomind.biblequizapp.model.Question;
import com.nextinnomind.biblequizapp.utils.JsonDataLoader;
import com.nextinnomind.biblequizapp.utils.MobileViewLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import static com.nextinnomind.biblequizapp.controller.LevelsController.CURRENT_LEVEL;
public class QuizLevelController {

    public Label levelName;
    @FXML private Button backButton;
    @FXML private Label questionLabel;
    @FXML private Label questionNumberLabel;
    @FXML private Label timerLabel;
    @FXML private Button choiceA, choiceB, choiceC, choiceD;

    private final List<Question> allQuestions = new ArrayList<>();
    private List<Question> selectedQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private double score = 0;
    private int correctCount = 0;

    private Thread timerThread;
    private boolean questionAnswered = false;
    LevelScore levelScore;


    @FXML
    public void initialize() {
        backButton.setOnAction(e -> handleBack());
        Collections.shuffle(allQuestions);
        levelName.setText(String.valueOf(CURRENT_LEVEL));

        JsonDataLoader.getInstance().setSelectedQuestions(15);
        selectedQuestions = JsonDataLoader.getInstance().getSelectedQuestions();
        loadQuestionsUI();
        loadQuestion(currentQuestionIndex);
        this.levelScore = new LevelScore();
        this.levelScore.setLevel(CURRENT_LEVEL);
        this.levelScore.setTotalQuestions(selectedQuestions.size());
        this.levelScore.setTimestamp(String.valueOf(System.currentTimeMillis()));

        choiceA.setOnAction(e -> handleChoice("A"));
        choiceB.setOnAction(e -> handleChoice("B"));
        choiceC.setOnAction(e -> handleChoice("C"));
        choiceD.setOnAction(e -> handleChoice("D"));
    }

    void loadQuestionsUI() {
          for (Question q : JsonDataLoader.getInstance().getQuestions()) {
                allQuestions.add(new Question(q.getQuestion(), q.getChoices(), q.getCorrectAnswer()));
            }

    }

    private void loadQuestion(int index) {
        stopTimer();
        if (index >= selectedQuestions.size()) {
            showScoreView();
            return;
        }

        Question q = selectedQuestions.get(index);
        questionLabel.setText(q.getQuestion());
        choiceA.setText("A. " + q.getChoices().get("A"));
        choiceB.setText("B. " + q.getChoices().get("B"));
        choiceC.setText("C. " + q.getChoices().get("C"));
        choiceD.setText("D. " + q.getChoices().get("D"));

        questionNumberLabel.setText("Question " + (index + 1) + " of " + selectedQuestions.size());
        resetChoiceButtons();

        questionAnswered = false;
        startTimer();
    }

    private void resetChoiceButtons() {
        for (Button btn : List.of(choiceA, choiceB, choiceC, choiceD)) {
            btn.setStyle(null);
            btn.setDisable(false);
        }
    }

    private void handleChoice(String selected) {
        if (questionAnswered) return;
        questionAnswered = true;
        stopTimer();

        Question current = selectedQuestions.get(currentQuestionIndex);

        choiceA.setDisable(true);
        choiceB.setDisable(true);
        choiceC.setDisable(true);
        choiceD.setDisable(true);

        Button selectedBtn = getButtonByKey(selected);

        if (selected.equals(current.getCorrectAnswer())) {
            score += (double) 100 /15;
            correctCount++;
            if (selectedBtn != null)
                selectedBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        } else {
            score += 0.1;
            if (selectedBtn != null)
                selectedBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
            Button correctBtn = getButtonByKey(current.getCorrectAnswer());
            if (correctBtn != null)
                correctBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        }


        new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            Platform.runLater(() -> {
                currentQuestionIndex++;
                loadQuestion(currentQuestionIndex);
            });
        }).start();
    }

    private void startTimer() {
        timerThread = new Thread(() -> {
            for (int i = 30; i >= 0; i--) {
                int finalI = i;
                Platform.runLater(() -> timerLabel.setText("Time Left: " + finalI + "s"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                if (questionAnswered) return;
            }

            questionAnswered = true;
            Platform.runLater(() -> {
                // No points if time runs out
                score += 0.0;
                currentQuestionIndex++;
                showScoreView();
            });
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    private void stopTimer() {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
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
        levelScore.setTimestamp(String.valueOf(System.currentTimeMillis()));
        JsonDataLoader.getInstance().saveScore(levelScore);
        JsonDataLoader.getInstance().saveOpenLevel(new OpenedLevel((levelScore.getLevel()+1)));
        try {
           Stage stage = (Stage) backButton.getScene().getWindow();
           MobileViewLoader.load(stage, "/com/nextinnomind/biblequizapp/views/score-view.fxml");
        } catch (IOException e) {
            System.out.println(405);
        }
    }

    private void handleBack() {
        stopTimer();
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            MobileViewLoader.load(stage, "/com/nextinnomind/biblequizapp/views/levels-view.fxml");
        } catch (IOException e) {
            System.out.println(405);
        }
    }


}
