package com.nextinnomind.biblequizapp.Style;

public class QuizViewStyles {
    public static final String DEFAULT_BUTTON_STYLE =
            "-fx-background-color: white; -fx-text-fill: #2C3E50; -fx-font-size: 16px; -fx-font-weight: 500; " +
                    "-fx-background-radius: 12px; -fx-padding: 15 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2); " +
                    "-fx-border-color: rgba(0,0,0,0.1); -fx-border-radius: 12px; -fx-border-width: 1; -fx-pref-width: 340; -fx-alignment: CENTER_LEFT;";

    public static final String HOVER_BUTTON_STYLE =
            "-fx-background-color: #f8f9fa; -fx-text-fill: #2C3E50; -fx-font-size: 16px; -fx-font-weight: 500; " +
                    "-fx-background-radius: 12px; -fx-padding: 15 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 12, 0, 0, 4); " +
                    "-fx-border-color: #667eea; -fx-border-radius: 12px; -fx-border-width: 2; -fx-pref-width: 340; -fx-alignment: CENTER_LEFT;";

    public static final String CORRECT_ANSWER_STYLE =
            "-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 500; " +
                    "-fx-background-radius: 12px; -fx-padding: 15 20;";

    public static final String INCORRECT_ANSWER_STYLE =
            "-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: 500; " +
                    "-fx-background-radius: 12px; -fx-padding: 15 20;";
}
