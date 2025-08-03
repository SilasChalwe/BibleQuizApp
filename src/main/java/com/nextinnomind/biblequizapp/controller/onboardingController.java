package com.nextinnomind.biblequizapp.controller;

import com.nextinnomind.biblequizapp.Loader.ViewLoader;
import com.nextinnomind.biblequizapp.Manager.ViewModeSelectorManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class onboardingController {

    private static final Logger logger = LogManager.getLogger(onboardingController.class);


    private final ViewModeSelectorManager viewModeSelectorManager = new ViewModeSelectorManager();

    @FXML
    private void handlePlayButton(ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String viewMode = viewModeSelectorManager.getViewMode();
        logger.info("Play button clicked. Detected view mode: {}", viewMode);
        ViewLoader.loadMainView(window,viewMode,"/com/nextinnomind/biblequizapp/views/levels-view.fxml");

    }
}
