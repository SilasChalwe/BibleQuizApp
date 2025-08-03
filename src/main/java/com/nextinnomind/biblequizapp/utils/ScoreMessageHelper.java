package com.nextinnomind.biblequizapp.utils;



import com.nextinnomind.biblequizapp.Loader.ImageLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class ScoreMessageHelper {
    private static final String[] SUCCESS_TITLES = {
            "Congratulations!", "Excellent Work!", "Outstanding!", "Well Done!"
    };

    private static final String[] SUCCESS_MESSAGES = {
            "You've successfully completed this level! Next level unlocked!",
            "Your knowledge is impressive! Ready for the next challenge?",
            "You're on fire! The next level awaits you!",
            "Fantastic performance! You've earned access to the next level!"
    };

    private static final String[] FAILURE_TITLES = {
            "Keep Trying! ", "Almost There! ", "Don't Give Up! ", "Practice Makes Perfect! "
    };

    private static final String[] FAILURE_MESSAGES = {
            "You need 50% or higher to unlock the next level. Keep practicing!",
            "You're getting stronger with each attempt! Aim for 50% to advance!",
            "Success is just around the corner. Score 50% to unlock the next level!",
            "Every expert was once a beginner. Get 50% to move forward!"
    };

    public void configureSuccessUI(ImageView trophyImage, Circle trophyBackground,
                                   Label resultTitle, Label resultMessage) {
        trophyImage.setImage(ImageLoader.imageLoad("/com/nextinnomind/biblequizapp/assets/img/praying-hand-logo.png"));
        trophyBackground.setStyle("-fx-fill: radial-gradient(center 50% 50%, radius 60%, " +
                "rgba(255,215,0,0.2), rgba(255,215,0,0.05)); -fx-stroke: #FFD700; -fx-stroke-width: 3;");

        int randomIndex = (int) (Math.random() * SUCCESS_TITLES.length);
        resultTitle.setText(SUCCESS_TITLES[randomIndex]);
        resultMessage.setText(SUCCESS_MESSAGES[randomIndex]);
        resultTitle.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 28px; " +
                "-fx-font-weight: bold; -fx-font-family: 'Palatino Linotype';");
    }

    public void configureFailureUI(ImageView trophyImage, Circle trophyBackground,
                                   Label resultTitle, Label resultMessage) {
        trophyImage.setImage(ImageLoader.imageLoad("/com/nextinnomind/biblequizapp/assets/img/praying-hand-logo.png"));
        trophyBackground.setStyle("-fx-fill: radial-gradient(center 50% 50%, radius 60%, " +
                "rgba(231,76,60,0.2), rgba(231,76,60,0.05)); -fx-stroke: #e74c3c; -fx-stroke-width: 3;");

        int randomIndex = (int) (Math.random() * FAILURE_TITLES.length);
        resultTitle.setText(FAILURE_TITLES[randomIndex]);
        resultMessage.setText(FAILURE_MESSAGES[randomIndex]);
        resultTitle.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 28px; " +
                "-fx-font-weight: bold; -fx-font-family: 'Palatino Linotype';");
    }
}