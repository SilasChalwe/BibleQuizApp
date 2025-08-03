package com.nextinnomind.biblequizapp.Style;


/**
 * Centralized dialog style configuration for JavaFX dialogs in BibleQuizApp.
 * Allows consistent theming for alert panes and buttons.
 */
public class DialogStyle {

    // Style for the entire dialog pane (background, font, padding,)
    public static final String ALERT_PANE_STYLE = "-fx-background-color: #ffffff;" +
            "-fx-font-family: 'Segoe UI';" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 20px;";

    //  button style (color, padding, shape)
    public static final String BUTTON_STYLE = "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 6px 12px;" +
            "-fx-background-radius: 5px;" +
            "-fx-border-radius: 5px;";
    public static  final   String PURPLE_STYLE = """
        -fx-background-color: #6a0dad;
        -fx-text-fill: white;
        -fx-font-size: 14px;
        -fx-padding: 15;
    """;
}
