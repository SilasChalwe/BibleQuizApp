package com.nextinnomind.biblequizapp.widget;


public class ScoreUIConstants {
    // Success titles and messages
    public static final String[] SUCCESS_TITLES = {
            "Congratulations!", "Excellent Work!", "Outstanding!", "Well Done!"
    };

    public static final String[] SUCCESS_MESSAGES = {
            "You've successfully completed this level! Next level unlocked!",
            "Your knowledge is impressive! Ready for the next challenge?",
            "You're on fire! The next level awaits you!",
            "Fantastic performance! You've earned access to the next level!"
    };

    // Failure titles and messages
    public static final String[] FAILURE_TITLES = {
            "Keep Trying! ", "Almost There! ", "Don't Give Up! ", "Practice Makes Perfect! "
    };

    public static final String[] FAILURE_MESSAGES = {
            "You need 50% or higher to unlock the next level. Keep practicing!",
            "You're getting stronger with each attempt! Aim for 50% to advance!",
            "Success is just around the corner. Score 50% to unlock the next level!",
            "Every expert was once a beginner. Get 50% to move forward!"
    };

    // Trophy styles
    public static final String SUCCESS_TROPHY_STYLE =
            "-fx-fill: radial-gradient(center 50% 50%, radius 60%, " +
                    "rgba(255,215,0,0.2), rgba(255,215,0,0.05)); " +
                    "-fx-stroke: #FFD700; -fx-stroke-width: 3;";

    public static final String FAILURE_TROPHY_STYLE =
            "-fx-fill: radial-gradient(center 50% 50%, radius 60%, " +
                    "rgba(231,76,60,0.2), rgba(231,76,60,0.05)); " +
                    "-fx-stroke: #e74c3c; -fx-stroke-width: 3;";

    public static final String SUCCESS_TITLE_STYLE =
            "-fx-text-fill: #27ae60; " +
                    "-fx-font-size: 28px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-font-family: 'Palatino Linotype';";

    public static final String FAILURE_TITLE_STYLE =
            "-fx-text-fill: #e74c3c; " +
                    "-fx-font-size: 28px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-font-family: 'Palatino Linotype';";

    // Image paths
    public static final String TROPHY_W_IMAGE_PATH =    "/com/nextinnomind/biblequizapp/assets/img/won.png";
    public static final String TROPHY_F_IMAGE_PATH =     "/com/nextinnomind/biblequizapp/assets/img/game-over.png";
}