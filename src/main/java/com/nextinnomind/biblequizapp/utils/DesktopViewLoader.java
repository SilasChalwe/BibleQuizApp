package com.nextinnomind.biblequizapp.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DesktopViewLoader {

    private static final double INIT_WIDTH = 700;
    private static final double INIT_HEIGHT = 700;

    private static final double MIN_WIDTH =500;
    private static final double MIN_HEIGHT = 650;

    public static void load(Stage stage, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(DesktopViewLoader.class.getResource(fxmlPath));
        Region content = loader.load();

        Scene scene = new Scene(content, INIT_WIDTH, INIT_HEIGHT);
        scene.getStylesheets().add(
                DesktopViewLoader.class.getResource("/com/nextinnomind/biblequizapp/static/style.css").toExternalForm()
        );

        // Make window responsive and resizable
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setResizable(true);

        if (!stage.isShowing()) {
            stage.initStyle(StageStyle.DECORATED);
        }

        stage.setScene(scene);

        // Use current stage size if different from initial; otherwise use initial size
        double widthToUse = (stage.getWidth() > 0 && stage.getWidth() != INIT_WIDTH) ? stage.getWidth() : INIT_WIDTH;
        double heightToUse = (stage.getHeight() > 0 && stage.getHeight() != INIT_HEIGHT) ? stage.getHeight() : INIT_HEIGHT;

        stage.setWidth(widthToUse);
        stage.setHeight(heightToUse);

        // Center the window on screen only if size is initial (optional)
        if (widthToUse == INIT_WIDTH && heightToUse == INIT_HEIGHT) {
            stage.centerOnScreen();
        }

        if (!stage.isShowing()) {
            stage.show();
        }

        // Bind Region to Scene for automatic resize
        content.prefWidthProperty().bind(scene.widthProperty());
        content.prefHeightProperty().bind(scene.heightProperty());
    }
}
