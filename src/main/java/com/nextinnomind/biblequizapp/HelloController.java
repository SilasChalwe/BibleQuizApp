package com.nextinnomind.biblequizapp;

import com.nextinnomind.biblequizapp.utils.DesktopViewLoader;
import com.nextinnomind.biblequizapp.utils.MobileViewLoader;
import com.nextinnomind.biblequizapp.utils.ViewModeSelector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class HelloController {

    private static final Logger logger = LogManager.getLogger(HelloController.class);

    @FXML
    private Button playButton;

    private final ViewModeSelector viewModeSelector = new ViewModeSelector();

    @FXML
    private void handlePlayButton(ActionEvent event) {
        try {
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            String viewMode = viewModeSelector.getViewMode();

            logger.info("Play button clicked. Detected view mode: {}", viewMode);

            String fxmlPath = "/com/nextinnomind/biblequizapp/views/levels-view.fxml";

            if ("mobile".equalsIgnoreCase(viewMode)) {
                MobileViewLoader.load(window, fxmlPath);
                logger.info("Loaded mobile view: {}", fxmlPath);
            } else {
                DesktopViewLoader.load(window, fxmlPath);
                logger.info("Loaded desktop view: {}", fxmlPath);
            }

        } catch (IOException e) {
            logger.error("Error loading levels view FXML", e);
        }
    }
}
