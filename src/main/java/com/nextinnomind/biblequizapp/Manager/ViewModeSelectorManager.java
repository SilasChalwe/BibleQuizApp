package com.nextinnomind.biblequizapp.Manager;

import com.nextinnomind.biblequizapp.Style.DialogStyle;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.StageStyle;

import java.util.Optional;
import java.util.prefs.Preferences;

public class ViewModeSelectorManager {

    private static final String PREF_KEY = "app_view_mode";
    private static final String VIEW_MOBILE = "mobile";
    private static final String VIEW_DESKTOP = "desktop";

    private final Preferences prefs = Preferences.userNodeForPackage(ViewModeSelectorManager.class);

    public String getViewMode() {
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

    private Optional<String> showChoiceDialog(String defaultMode) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Select View Mode");
        alert.setHeaderText("Choose your preferred view mode");
        alert.setContentText("Detected device size suggests " + defaultMode + " view.\n"
                + "Please select which view mode you want to use:");


        alert.getDialogPane().setStyle(DialogStyle.ALERT_PANE_STYLE);

        ButtonType mobileButton = new ButtonType("Mobile View", ButtonBar.ButtonData.OK_DONE);
        ButtonType desktopButton = new ButtonType("Desktop View", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(mobileButton, desktopButton);

        try {
            var mobileBtn = alert.getDialogPane().lookupButton(mobileButton);
            var desktopBtn = alert.getDialogPane().lookupButton(desktopButton);

            if (mobileBtn != null) mobileBtn.setStyle(DialogStyle.BUTTON_STYLE);
            if (desktopBtn != null) desktopBtn.setStyle(DialogStyle.BUTTON_STYLE);
        } catch (Exception e) {
            System.out.println("Warning: Could not style dialog buttons: " + e.getMessage());
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == mobileButton) return Optional.of(VIEW_MOBILE);
            if (result.get() == desktopButton) return Optional.of(VIEW_DESKTOP);
        }

        return Optional.empty();
    }

    public void clearSavedChoice() {
        prefs.remove(PREF_KEY);
    }
}
