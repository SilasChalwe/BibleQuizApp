package com.nextinnomind.biblequizapp.Style;

public class LevelStyle {

    public static final String BACKGROUND_COLOR = "-fx-background-color: #1E144F";

    public static final String[] LEVEL_COLORS = {
            "#6BCB77", "#4D96FF", "#A66DD4", "#FF6B6B",
            "#00C2D1", "#FF7B9C", "#FF8C42", "#ADFF2F",
            "#6F38C5", "#0FC2C0", "#9C27B0", "#3F51B5"
    };

    public static final String CARD_STYLE = "-fx-border-radius: 10; -fx-background-radius: 10;";
    public static final String BADGE_STYLE = """
        -fx-background-radius: 30;
        -fx-background-color: linear-gradient(to bottom right, #3A3A3A, #1E1E1E);
        -fx-border-radius: 30;
        -fx-border-color: white;
        -fx-border-width: 2;
        """;

    public static final String PLAY_BUTTON_UNLOCKED = "-fx-background-color: #6A11CB; -fx-text-fill: white;";
    public static final String PLAY_BUTTON_LOCKED = "-fx-background-color: #4B0082; -fx-text-fill: white;";

    public static final String LOCK_OVERLAY = """
        -fx-background-color: rgba(75, 0, 130, 0.75);
        -fx-background-radius: 10;
        -fx-border-radius: 10;
        """;

    public static final String LOCK_CIRCLE = """
        -fx-background-radius: 9;
        -fx-background-color: rgba(186, 85, 211, 0.25);
        """;
}
