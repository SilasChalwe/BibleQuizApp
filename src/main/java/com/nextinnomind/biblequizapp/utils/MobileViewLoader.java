package com.nextinnomind.biblequizapp.utils;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MobileViewLoader {

    private static final Logger logger = LogManager.getLogger(MobileViewLoader.class);

    private static final double WIDTH = 360;
    private static final double HEIGHT = 640;

    /**
     * Loads the specified FXML into the given Stage,
     * styled to simulate a mobile device with rounded corners and drag support.
     *
     * Logs whether touch input is supported.
     *
     * @param stage    the Stage to load into
     * @param fxmlPath path to the FXML file
     * @throws IOException if loading FXML fails
     */
    public static void load(Stage stage, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(MobileViewLoader.class.getResource(fxmlPath));
        Region content = loader.load();

        StackPane root = new StackPane(content);
        root.setPrefSize(WIDTH, HEIGHT);

        root.setStyle(
                "-fx-background-color: #1E144F;" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-radius: 30;" +
                        "-fx-border-color: transparent;"
        );

        Rectangle clip = new Rectangle(WIDTH, HEIGHT);
        clip.setArcWidth(60);
        clip.setArcHeight(60);
        root.setClip(clip);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);

        if (!stage.isShowing()) {
            stage.initStyle(StageStyle.TRANSPARENT);
        }

        stage.setScene(scene);

        // Dragging support to move window by dragging anywhere on root pane
        final double[] dragDelta = new double[2];
        root.setOnMousePressed(event -> {
            dragDelta[0] = stage.getX() - event.getScreenX();
            dragDelta[1] = stage.getY() - event.getScreenY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + dragDelta[0]);
            stage.setY(event.getScreenY() + dragDelta[1]);
        });

        // Touch support detection: register a temporary listener and check if any touch event is fired.
        final boolean[] touchSupported = {false};
        EventHandler<TouchEvent> touchEventHandler = new EventHandler<>() {
            @Override
            public void handle(TouchEvent event) {
                touchSupported[0] = true;
                logger.info("Touch event detected: system supports touch input.");
                // Remove this handler after first detection
                root.removeEventHandler(TouchEvent.ANY, this);
            }
        };
        root.addEventHandler(TouchEvent.ANY, touchEventHandler);

        logger.info("MobileViewLoader loading '{}'. Touch support detected? {}", fxmlPath, touchSupported[0]);

        if (!stage.isShowing()) {
            stage.show();
            logger.info("Stage shown with mobile view style. Width: {}, Height: {}", WIDTH, HEIGHT);
        }
    }
}
