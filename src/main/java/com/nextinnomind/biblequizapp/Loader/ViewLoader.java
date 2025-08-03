package com.nextinnomind.biblequizapp.Manager;

import com.nextinnomind.biblequizapp.Display.DesktopDisplay;
import com.nextinnomind.biblequizapp.Display.MobileDisplay;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ViewLoader {

    private static final Logger logger = LogManager.getLogger(ViewLoader.class);

    public static void loadMainView(Stage stage, String viewMode) {
        try {
            String fxmlPath = "/com/nextinnomind/biblequizapp/views/onboarding-view.fxml";

            if ("mobile".equalsIgnoreCase(viewMode)) {
                MobileDisplay.load(stage, fxmlPath);
                logger.info("Mobile view loaded.");
            } else {
                DesktopDisplay.load(stage, fxmlPath);
                logger.info("Desktop view loaded.");
            }

            stage.setOnCloseRequest(event -> {
                logger.info("Stage close requested.");
                AudioManager.stopAllAudio();
                ShutdownManager.cleanup(null);
            });

        } catch (Exception e) {
            logger.error("Failed to load main view: {}", e.getMessage(), e);
            Platform.exit();
        }
    }
}