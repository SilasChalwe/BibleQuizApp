//package com.nextinnomind.biblequizapp.utils;
//
//import javafx.geometry.Rectangle2D;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonBar;
//import javafx.scene.control.ButtonType;
//import javafx.stage.Modality;
//import javafx.stage.Screen;
//import javafx.stage.StageStyle;
//
//import java.util.Optional;
//import java.util.prefs.Preferences;
//
//public class ViewModeSelector {
//
//    private static final String PREF_KEY = "app_view_mode";
//    private static final String VIEW_MOBILE = "mobile";
//    private static final String VIEW_DESKTOP = "desktop";
//
//    private final Preferences prefs = Preferences.userNodeForPackage(ViewModeSelector.class);
//
//    /**
//     * Returns the selected view mode ("mobile" or "desktop").
//     * If no previous choice is saved, detects device size and asks user once.
//     */
//    public String getViewMode() {
//        String savedMode = prefs.get(PREF_KEY, null);
//        if (savedMode != null) {
//            return savedMode;
//        }
//
//        // Detect screen size
//        Rectangle2D bounds = Screen.getPrimary().getBounds();
//        double width = bounds.getWidth();
//
//        String defaultMode = (width < 800) ? VIEW_MOBILE : VIEW_DESKTOP;
//
//        // Ask user once
//        Optional<String> choice = showChoiceDialog(defaultMode);
//
//        String chosenMode = choice.orElse(defaultMode); // fallback to default if closed
//
//        prefs.put(PREF_KEY, chosenMode);
//
//        return chosenMode;
//    }
//
//    /**
//     * Shows a dialog with explicit Mobile and Desktop buttons.
//     * Returns the choice as "mobile" or "desktop".
//     */
//    private Optional<String> showChoiceDialog(String defaultMode) {
//        Alert alert = new Alert(Alert.AlertType.NONE);
//        alert.initModality(Modality.APPLICATION_MODAL);
//        alert.initStyle(StageStyle.UTILITY);
//        alert.setTitle("Select View Mode");
//        alert.setHeaderText("Choose your preferred view mode");
//        alert.setContentText("Detected device size suggests " + defaultMode + " view.\n"
//                + "Please select which view mode you want to use:");
//
//        // Create explicit buttons
//        ButtonType mobileButton = new ButtonType("Mobile View", ButtonBar.ButtonData.OK_DONE);
//        ButtonType desktopButton = new ButtonType("Desktop View", ButtonBar.ButtonData.CANCEL_CLOSE);
//
//        alert.getButtonTypes().setAll(mobileButton, desktopButton);
//
//        // Show and wait for user selection
//        Optional<ButtonType> result = alert.showAndWait();
//
//        if (result.isPresent()) {
//            if (result.get() == mobileButton) {
//                return Optional.of(VIEW_MOBILE);
//            } else if (result.get() == desktopButton) {
//                return Optional.of(VIEW_DESKTOP);
//            }
//        }
//
//        // Dialog was closed without explicit choice: return empty
//        return Optional.empty();
//    }
//
//    /**
//     * Clears saved user choice so user will be asked again next start.
//     */
//    public void clearSavedChoice() {
//        prefs.remove(PREF_KEY);
//    }
//}






package com.nextinnomind.biblequizapp.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.StageStyle;

import java.util.Optional;
import java.util.prefs.Preferences;

public class ViewModeSelector {

    private static final String PREF_KEY = "app_view_mode";
    private static final String VIEW_MOBILE = "mobile";
    private static final String VIEW_DESKTOP = "desktop";

    private final Preferences prefs = Preferences.userNodeForPackage(ViewModeSelector.class);

    /**
     * Returns the selected view mode ("mobile" or "desktop").
     * If no previous choice is saved, detects device size and asks user once.
     */
    public String getViewMode() {
        String savedMode = prefs.get(PREF_KEY, null);
        if (savedMode != null) {
            return savedMode;
        }

        // Detect screen size
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double width = bounds.getWidth();

        String defaultMode = (width < 800) ? VIEW_MOBILE : VIEW_DESKTOP;

        // Ask user once
        Optional<String> choice = showChoiceDialog(defaultMode);

        String chosenMode = choice.orElse(defaultMode); // fallback to default if closed

        prefs.put(PREF_KEY, chosenMode);

        return chosenMode;
    }

    /**
     * Shows a dialog with explicit Mobile and Desktop buttons.
     * Returns the choice as "mobile" or "desktop".
     * Applies inline styling to match your app's theme.
     */
    private Optional<String> showChoiceDialog(String defaultMode) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Screen Mode Selector");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Select View Mode");
        alert.setHeaderText("Choose your preferred view mode");
        alert.setContentText("Detected device size suggests " + defaultMode + " view.\n"
                + "Please select which view mode you want to use:");

        // Apply inline styling to the dialog
        alert.getDialogPane().setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-border-color: #ffffff;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;"+"-fx-text-fill: white;"
        );

        // Create explicit buttons
        ButtonType mobileButton = new ButtonType("Mobile View", ButtonBar.ButtonData.OK_DONE);
        ButtonType desktopButton = new ButtonType("Desktop View", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(mobileButton, desktopButton);

        // Apply inline styling to buttons
        try {
            var mobileBtn = alert.getDialogPane().lookupButton(mobileButton);
            var desktopBtn = alert.getDialogPane().lookupButton(desktopButton);

            if (mobileBtn != null) {
                mobileBtn.setStyle(
                        "-fx-background-color: #2575FC;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 8px 16px;" +
                                "-fx-background-radius: 6px;" +
                                "-fx-cursor: hand;"
                );
            }

            if (desktopBtn != null) {
                desktopBtn.setStyle(
                        "-fx-background-color: #2575FC;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 8px 16px;" +
                                "-fx-background-radius: 6px;" +
                                "-fx-cursor: hand;"
                );
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not style dialog buttons: " + e.getMessage());
        }

        // Show and wait for user selection
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == mobileButton) {
                return Optional.of(VIEW_MOBILE);
            } else if (result.get() == desktopButton) {
                return Optional.of(VIEW_DESKTOP);
            }
        }

        // Dialog was closed without explicit choice: return empty
        return Optional.empty();
    }

    /**
     * Clears saved user choice so user will be asked again next start.
     */
    public void clearSavedChoice() {
        prefs.remove(PREF_KEY);
    }
}