package com.nextinnomind.biblequizapp.Loader;
import com.nextinnomind.biblequizapp.controller.QuizViewController;
import com.nextinnomind.biblequizapp.model.LevelScore;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.nextinnomind.biblequizapp.controller.LevelsViewController.CURRENT_LEVEL;

public class ScoreLoader {
    private static final Logger logger = LogManager.getLogger(ScoreLoader.class);
    private final DataLoader dataLoader = DataLoader.getInstance();
    // Getters
    @Getter
    private LevelScore currentScore;
    @Getter
    private boolean isPassed;
    private boolean wasScoreSaved;

    public void loadScoreData() {
        loadCurrentScore();
        calculatePassStatus();
        checkSavedScore();
        logScoreInfo();
    }

    private void loadCurrentScore() {
        currentScore = QuizViewController.CURRENT_QUIZ_SCORE;

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
        isPassed = percentage >= 50.0;
    }

    private void checkSavedScore() {
        LevelScore savedScore = dataLoader.getLatestScore(CURRENT_LEVEL);
        wasScoreSaved = savedScore != null && savedScore.getScore() >= 50.0;
    }

    private void logScoreInfo() {
        logger.info("Loaded score data: Level {}, Score: {}, Passed: {}, Saved: {}",
                CURRENT_LEVEL, String.format("%.1f", currentScore.getScore()), isPassed, wasScoreSaved);
    }

    public boolean wasScoreSaved() {
        return wasScoreSaved;
    }
}