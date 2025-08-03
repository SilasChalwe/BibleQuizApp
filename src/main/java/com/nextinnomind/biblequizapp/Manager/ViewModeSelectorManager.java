package com.nextinnomind.biblequizapp.Manager;

import com.nextinnomind.biblequizapp.Style.DialogStyle;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.StageStyle;

import java.util.Optional;
import java.util.prefs.Preferences;

import static com.nextinnomind.biblequizapp.Style.DialogStyle.BUTTON_STYLE;
import static com.nextinnomind.biblequizapp.Style.DialogStyle.PURPLE_STYLE;

public class ViewModeSelectorManager {

    private static final String PREF_KEY = "app_view_mode";
    public static final String VIEW_MOBILE = "mobile";
    public static final String VIEW_DESKTOP = "desktop";

    private static final Preferences prefs = Preferences.userNodeForPackage(ViewModeSelectorManager.class);

    public static String getViewMode() {
        String savedMode = prefs.get(PREF_KEY, null);
        if (savedMode != null) {
            return savedMode;
        }

        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double width = bounds.getWidth();
        String defaultMode = (width < 800) ? VIEW_MOBILE : VIEW_DESKTOP;

        Optional<String> choice = showChoiceDialog(defaultMode);
        String chosenMode = choice.orElse(defaultMode);
        prefs.put(PREF_KEY, chosenMode);
        return chosenMode;
    }


    private static Optional<String> showChoiceDialog(String defaultMode) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UNDECORATED); // remove window decoration
        alert.setTitle("Select View Mode");
        alert.setHeaderText("Choose your preferred view mode");
        alert.setContentText("Detected device size suggests " + defaultMode + " view.\n"
                + "Please select which view mode you want to use:");

        // Apply purple background to dialog
        DialogPane pane = alert.getDialogPane();
        pane.setStyle("-fx-background-color: #6A0DAD; -fx-border-radius: 10; -fx-background-radius: 10;");
        pane.lookup(".content.label").setStyle("-fx-text-fill: white;");
        pane.lookup(".header-panel").setStyle("-fx-text-fill: white;");

        ButtonType mobileButton = new ButtonType("Mobile", ButtonBar.ButtonData.OK_DONE);
        ButtonType desktopButton = new ButtonType("Desktop", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(mobileButton, desktopButton);

        Platform.runLater(() -> {
            var mobileBtn = (Button) alert.getDialogPane().lookupButton(mobileButton);
            var desktopBtn = (Button) alert.getDialogPane().lookupButton(desktopButton);

            if (mobileBtn != null) {
                mobileBtn.setStyle("-fx-background-color: white; -fx-text-fill: #6A0DAD; -fx-font-weight: bold; -fx-background-radius: 5;");
                mobileBtn.setOnMouseEntered(e -> mobileBtn.setStyle("-fx-background-color: #B266FF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));
                mobileBtn.setOnMouseExited(e -> mobileBtn.setStyle("-fx-background-color: white; -fx-text-fill: #6A0DAD; -fx-font-weight: bold; -fx-background-radius: 5;"));
            }

            if (desktopBtn != null) {
                desktopBtn.setStyle("-fx-background-color: white; -fx-text-fill: #6A0DAD; -fx-font-weight: bold; -fx-background-radius: 5;");
                desktopBtn.setOnMouseEntered(e -> desktopBtn.setStyle("-fx-background-color: #B266FF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));
                desktopBtn.setOnMouseExited(e -> desktopBtn.setStyle("-fx-background-color: white; -fx-text-fill: #6A0DAD; -fx-font-weight: bold; -fx-background-radius: 5;"));
            }
        });

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == mobileButton) return Optional.of(VIEW_MOBILE);
            if (result.get() == desktopButton) return Optional.of(VIEW_DESKTOP);
        }

        return Optional.empty();
    }

    public static void clearSavedChoice() {
        prefs.remove(PREF_KEY);
    }
}